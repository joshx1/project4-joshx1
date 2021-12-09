package User;

import ConnectionPool.DBCPDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.ClientInfo;
import utilities.DBUtilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import utilities.DBUtilities;
import ConnectionPool.DBCPDataSource;

public class CreateEventInputServlet extends HttpServlet {

    private ClientInfo clientInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String sessionId = req.getSession(true).getId();
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilities.emailFromSessionId(connection, sessionId);
            clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            resp.getWriter().println("<h1> Create Event </h1>");
            resp.getWriter().println("<form target=\"_blank\" action=\"/api/event/"+ clientInfo.getEmail() + "\" method=\"post\">\n" +
                "  <label for=\"msg\">Event name:</label><br/>\n" +
                "  <input type=\"text\" id=\"name\" name=\"name\"/><br/>\n" +
                "  <label for=\"msg\">Event date (YYYY-MM-DD):</label><br/>\n" +
                "  <input type=\"text\" id=\"date\" name=\"date\"/><br/>\n" +
                "  <label for=\"msg\">Location:</label><br/>\n" +
                "  <input type=\"text\" id=\"location\" name=\"location\"/><br/>\n" +
                "  <label for=\"msg\">Price:</label><br/>\n" +
                "  <input type=\"text\" id=\"price\" name=\"price\"/><br/>\n" +
                "  <label for=\"msg\">Student price:</label><br/>\n" +
                "  <input type=\"text\" id=\"priceStudent\" name=\"priceStudent\"/><br/>\n" +
                "  <label for=\"msg\">VIP price:</label><br/>\n" +
                "  <input type=\"text\" id=\"priceVIP\" name=\"priceVIP\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}