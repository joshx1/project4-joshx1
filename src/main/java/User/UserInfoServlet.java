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

/**
 * Implements logic for the /login path where Slack will redirect requests after
 * the user has entered their auth info.
 */
public class UserInfoServlet extends HttpServlet {

    private ClientInfo clientInfo;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilities.emailFromSessionId(connection, sessionId);
            clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            System.out.println(clientInfo.getName());
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1> User information </h1>");
            resp.getWriter().println("<h1>Name: " + clientInfo.getName() + "</h1>");
            resp.getWriter().println("<h1>Location: " + clientInfo.getLocation() + "</h1>");
            resp.getWriter().println("<form action=\"/userinfo\" method=\"post\">\n" +
                "  <label for=\"msg\">New location:</label><br/>\n" +
                "  <input type=\"text\" id=\"location\" name=\"location\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpStatus.OK_200);
        String [] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        System.out.println(Arrays.toString(bodyParts));
        if (bodyParts[0].equals("name") && bodyParts.length == 2) {
            System.out.println(URI[3]);
            System.out.println(bodyParts[1]);
            try {
                Connection connection = DBCPDataSource.getConnection();
                DBUtilities.executeInsertClientName(connection, URI[3], bodyParts[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (bodyParts[0].equals("location") && bodyParts.length == 2) {
            System.out.println(URI[3]);
            System.out.println(bodyParts[1]);
            try {
                Connection connection = DBCPDataSource.getConnection();
                DBUtilities.executeInsertClientLocation(connection, URI[3], bodyParts[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (bodyParts[0].equals("DOB") && bodyParts.length == 2) {
            try {
                Connection connection = DBCPDataSource.getConnection();
                DBUtilities.executeInsertClientDOB(connection, URI[3], Date.valueOf(bodyParts[1]));
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
