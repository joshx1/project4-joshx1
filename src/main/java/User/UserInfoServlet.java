package User;

import ConnectionPool.DBCPDataSource;
import org.apache.commons.io.IOUtils;
import utilities.DBUtilities;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import static utilities.VerifyAuthenticated.checkAuthentication;

/**
 * Handles all responsibility of displaying and editing the current user information.
 */
public class UserInfoServlet extends HttpServlet {

    private ClientInfo clientInfo;

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
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilities.emailFromSessionId(connection, sessionId);
            clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            System.out.println(clientInfo.getName());
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1> User information </h1>");
            resp.getWriter().println("<h1>Name: " + clientInfo.getName() + "</h1>");
            resp.getWriter().println("<form action=\"/userinfo/" + clientInfo.getEmail() + "\" method=\"post\">\n" +
                "  <label for=\"name\">New name:</label><br/>\n" +
                "  <input type=\"name\" id=\"name\" name=\"name\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
            resp.getWriter().println("<h1>Location: " + clientInfo.getLocation() + "</h1>");
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

            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        resp.setStatus(HttpStatus.OK_200);
        String [] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        System.out.println(Arrays.toString(bodyParts));
        if (bodyParts[0].equals("name") && bodyParts.length == 2) {
            try {
                Connection connection = DBCPDataSource.getConnection();
                DBUtilities.executeInsertClientName(connection, URI[2], bodyParts[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (bodyParts[0].equals("location") && bodyParts.length == 2) {
            try {
                Connection connection = DBCPDataSource.getConnection();
                DBUtilities.executeInsertClientLocation(connection, URI[2], bodyParts[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (bodyParts[0].equals("DOB") && bodyParts.length == 2) {
            try {
                Connection connection = DBCPDataSource.getConnection();
                DBUtilities.executeInsertClientDOB(connection, URI[2], Date.valueOf(bodyParts[1]));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Databases yes");
        resp.getWriter().println("<h1> Update success! </h1>");
        resp.getWriter().println("<form action=\"/login" + "\" method=\"get\">" +
            "<button name=\"returnhome\" value=" + ">Return to home</button>" +
            "</form>");
    }
}
