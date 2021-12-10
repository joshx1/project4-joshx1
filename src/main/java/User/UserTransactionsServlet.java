package User;

import ConnectionPool.DBCPDataSource;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;
import utilities.DBUtilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTransactionsServlet extends HttpServlet {
    ClientInfo clientInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilities.emailFromSessionId(connection, sessionId);
            clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            ResultSet resultSet = DBUtilities.eventsPurchased(connection, email);
            System.out.println(clientInfo.getName());
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1> User Transactions </h1>");
            while(resultSet.next()) {
                resp.getWriter().println("<h1>Name: " + resultSet.getString("name") + "</h1>");
                resp.getWriter().println("<p><a href=\"/event/" + resultSet.getInt("id") + "\">" + resultSet.getString("name") + "</a>");
                resp.getWriter().println("<form action=\"/transfer/" + resultSet.getInt("id") + "/" + resultSet.getString("type") + "\" method=\"post\">\n" +
                    "  <label for=\"msg\">Transfer ticket to (enter email):</label><br/>\n" +
                    "  <input type=\"text\" id=\"emailReceiver\" name=\"emailReceiver\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>");
            }
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
