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

public class CreateEventServlet extends HttpServlet {

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
                "  <label for=\"msg\">New location:</label><br/>\n" +
                "  <input type=\"text\" id=\"location\" name=\"location\"/><br/>\n" +
                "  <label for=\"msg\">New location:</label><br/>\n" +
                "  <input type=\"text\" id=\"blah\" name=\"blah\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
