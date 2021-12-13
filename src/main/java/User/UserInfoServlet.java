package User;

import ConnectionPool.DBCPDataSource;
import org.apache.commons.io.IOUtils;
import utilities.DBUtilitiesClient;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import static utilities.VerifyAuthenticated.checkAuthentication;

/**
 * Handles all responsibility of displaying and editing the current user information.
 */
public class UserInfoServlet extends HttpServlet {

    /**
     * Shows the user all of their relevant user information and allows them to input changes.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        if (checkAuthentication(req, resp, sessionId)) return;
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
            ClientInfo clientInfo = DBUtilitiesClient.userInfoFromEmail(connection, email);
            resp.getWriter().println("<h1> User information </h1>");
            resp.getWriter().println("<h1>Name: " + URLDecoder.decode(clientInfo.getName(), "UTF-8") + "</h1>");
            resp.getWriter().println("<form action=\"/userinfo/" + clientInfo.getEmail() + "\" method=\"post\">\n" +
                "  <label for=\"name\">New name:</label><br/>\n" +
                "  <input type=\"name\" id=\"name\" name=\"name\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
            resp.getWriter().println("<h1>Location: " + URLDecoder.decode(clientInfo.getLocation(), "UTF-8") + "</h1>");
            resp.getWriter().println("<form action=\"/userinfo/" + clientInfo.getEmail() + "\" method=\"post\">\n" +
                "  <label for=\"msg\">New location:</label><br/>\n" +
                "  <input type=\"text\" id=\"location\" name=\"location\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
            resp.getWriter().println("<h1>Date of Birth: " + clientInfo.getDob() + "</h1>");
            resp.getWriter().println("<form action=\"/userinfo/" + clientInfo.getEmail() + "\" method=\"post\">\n" +
                "  <label for=\"DOB\">New date of birth:</label><br/>\n" +
                "  <input type=\"date\" id=\"DOB\" name=\"DOB\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
            resp.getWriter().println(TicketServerConstants.RETURN_HOME);
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException e) {
            resp.getWriter().println(TicketServerConstants.ERROR + TicketServerConstants.RETURN_HOME);
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
            return;
        }
        resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        return;
    }

    /**
     * Takes the users inputted changes and sends them to database.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();
        if (checkAuthentication(req, resp, sessionId)) return;
        String [] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        try {
            Connection connection = DBCPDataSource.getConnection();
            if (bodyParts[0].equals("name") && bodyParts.length == 2) {
                    DBUtilitiesClient.executeInsertClientName(connection, URI[2], bodyParts[1]);
            } else if (bodyParts[0].equals("location") && bodyParts.length == 2) {
                DBUtilitiesClient.executeInsertClientLocation(connection, URI[2], bodyParts[1]);
            } else if (bodyParts[0].equals("DOB") && bodyParts.length == 2) {
                DBUtilitiesClient.executeInsertClientDOB(connection, URI[2], Date.valueOf(bodyParts[1]));
            }
        } catch (SQLException e) {
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println(TicketServerConstants.ERROR + TicketServerConstants.RETURN_HOME);
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
            return;
        }
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
        resp.getWriter().println(TicketServerConstants.SUCCESS + TicketServerConstants.RETURN_HOME);
        resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        return;
    }
}
