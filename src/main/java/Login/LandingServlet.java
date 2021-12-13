package Login;

import ConnectionPool.DBCPDataSource;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.Config;
import utilities.DBUtilitiesClient;
import utilities.LoginUtilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Landing page that allows a user to request to login with Slack.
 */
public class LandingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(TicketServerConstants.CLIENT_INFO_KEY);
        boolean flag = false;
        if (clientInfoObj != null) {
            try {
                Connection connection = DBCPDataSource.getConnection();
                String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
                flag = true;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
            if (flag == true) {
                // already authed, no need to log in
                resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
                resp.getWriter().println("<h1>You have already been authenticated</h1>");
                resp.getWriter().println("<form action=\"/login" + "\" method=\"get\">" +
                    "<button name=\"returnhome\" value=" + ">Return to home.</button>" +
                    "</form>");
                resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
                return;
            }
        }
        if (flag == false) {

            // retrieve the config info from the context
            Config config = (Config) req.getServletContext().getAttribute(TicketServerConstants.CONFIG_KEY);

            /** From the OpenID spec:
             * state
             * RECOMMENDED. Opaque value used to maintain state between the request and the callback.
             * Typically, Cross-Site Request Forgery (CSRF, XSRF) mitigation is done by cryptographically
             * binding the value of this parameter with a browser cookie.
             *
             * Use the session ID for this purpose.
             */
            String state = sessionId;

            /** From the Open ID spec:
             * nonce
             * OPTIONAL. String value used to associate a Client session with an ID Token, and to mitigate
             * replay attacks. The value is passed through unmodified from the Authentication Request to
             * the ID Token. Sufficient entropy MUST be present in the nonce values used to prevent attackers
             * from guessing values. For implementation notes, see Section 15.5.2.
             */
            String nonce = LoginUtilities.generateNonce(state);

            // Generate url for request to Slack
            String url = LoginUtilities.generateSlackAuthorizeURL(config.getClient_id(),
                state,
                nonce,
                config.getRedirect_url());

            resp.setStatus(HttpStatus.OK_200);
            PrintWriter writer = resp.getWriter();
            writer.println(TicketServerConstants.PAGE_HEADER);
            writer.println("<h1>Welcome to the Login with Slack Demo Application</h1>");
            writer.println("<a href=\"" + url + "\"><img src=\"" + TicketServerConstants.BUTTON_URL + "\"/></a>");
            writer.println(TicketServerConstants.PAGE_FOOTER);
        }
    }
}
