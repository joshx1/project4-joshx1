package Login;

import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;
import utilities.Config;
import utilities.HTTPFetcher;
import utilities.LoginUtilities;
import java.sql.Connection;
import java.sql.SQLException;
import ConnectionPool.DBCPDataSource;
import utilities.DBUtilitiesClient;

import java.io.IOException;
import java.util.Map;
import java.net.URLDecoder;

import static utilities.VerifyAuthenticated.checkAuthentication;

/**
 * Implements logic for the /login path where Slack will redirect requests after
 * the user has entered their auth info.
 * Referenced Sami's code at https://github.com/CS601-F21/code-examples.
 */
public class LoginServlet extends HttpServlet {

    /**
     * This method handles the login flow from Slack.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(TicketServerConstants.CLIENT_INFO_KEY);
        ClientInfo clientInfo = null;
        if(clientInfoObj != null) {
            // already authed, no need to log in
            //Connection connection = null;
            try {
                Connection connection = DBCPDataSource.getConnection();
                String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
                clientInfo = DBUtilitiesClient.userInfoFromEmail(connection, email);
            } catch (SQLException e) {
                e.printStackTrace();
                resp.setStatus(HttpStatus.UNAUTHORIZED_401);
                resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
                req.getSession().setAttribute(TicketServerConstants.CLIENT_INFO_KEY, null);
                resp.getWriter().println("<h1>Failed to retrieve information.</h1>");
                resp.getWriter().println("<form action=\"/" + "\" method=\"get\">" +
                    "<button name=\"returnhome\" value=" + ">Return to login again.</button>" +
                    "</form>");
                resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
                return;
            }
        } else {

            // retrieve the config info from the context
            Config config = (Config) req.getServletContext().getAttribute(TicketServerConstants.CONFIG_KEY);

            // retrieve the code provided by Slack
            String code = req.getParameter(TicketServerConstants.CODE_KEY);

            // generate the url to use to exchange the code for a token:
            // After the user successfully grants your app permission to access their Slack profile,
            // they'll be redirected back to your service along with the typical code that signifies
            // a temporary access code. Exchange that code for a real access token using the
            // /openid.connect.token method.
            String url = LoginUtilities.generateSlackTokenURL(config.getClient_id(), config.getClient_secret(), code, config.getRedirect_url());

            // Make the request to the token API
            String responseString = HTTPFetcher.doGet(url, null);
            Map<String, Object> response = LoginUtilities.jsonStrToMap(responseString);

            clientInfo = LoginUtilities.verifyTokenResponse(response, sessionId);
            if (clientInfo == null) {
                if (checkAuthentication(req, resp, sessionId)) return;
            }

            try {
                Connection connection = DBCPDataSource.getConnection();
                DBUtilitiesClient.executeInsertSessionData(connection, sessionId, clientInfo.getEmail());
                if (!(DBUtilitiesClient.checkClientExists(connection, clientInfo.getEmail()) == 1)) {
                    DBUtilitiesClient.executeInsertClientData(connection, clientInfo.getName(), clientInfo.getAccess_token(), clientInfo.getToken_type(), clientInfo.getId_token(), clientInfo.getEmail(), clientInfo.isEmail_verified());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            req.getSession().setAttribute(TicketServerConstants.CLIENT_INFO_KEY, clientInfo);
        }
        if(clientInfo == null) {
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>Oops, login unsuccessful</h1>");
            resp.getWriter().println("<form action=\"/" + "\" method=\"get\">" +
                "<button name=\"returnhome\" value=" + ">Return to login again.</button>" +
                "</form>");
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);

        } else {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<p><a href=\"/userinfo\">User Information</a>");
            resp.getWriter().println("<p><a href=\"/usertransactions\">User Transactions</a>");
            resp.getWriter().println("<p><a href=\"/events\">Event List</a>");
            resp.getWriter().println("<p><a href=\"/search\">Search Events</a>");
            resp.getWriter().println("<p><a href=\"/createevent\">Create Event</a>");
            resp.getWriter().println("<p><a href=\"/logout\">Signout</a>");
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        }
    }
}
