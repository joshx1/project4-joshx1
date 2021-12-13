package User;

import ConnectionPool.DBCPDataSource;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;
import utilities.DBUtilitiesClient;
import utilities.DBUtilitiesEvents;
import utilities.EventInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utilities.VerifyAuthenticated.checkAuthentication;

/**
 * Shows a users brought tickets and user transfer ticket functionality.
 */
public class UserTransactionsServlet extends HttpServlet {
    ClientInfo clientInfo;

    /**
     * Displays a page to user which shows the tickets they have purchased and allows them to transfer these tickets.
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
            String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
            clientInfo = DBUtilitiesClient.userInfoFromEmail(connection, email);
            ResultSet resultSet = DBUtilitiesEvents.eventsPurchased(connection, email);
            System.out.println(clientInfo.getName());
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1> User Transactions </h1>");
            while(resultSet.next()) {
                EventInfo eventInfo = DBUtilitiesEvents.executeSelectSpecificEvent(connection, resultSet.getInt("event_id"));
                resp.getWriter().println("<h1>Name: " + eventInfo.getName() + "</h1>");
                resp.getWriter().println("<h1>Tickets purchased: " + resultSet.getInt("amount") + " " + resultSet.getString("ticket_type") + " tickets" + "</h1>");
                resp.getWriter().println("<p><a href=\"/event/" + eventInfo.getId() + "\">" + eventInfo.getName() + "</a>");
                resp.getWriter().println("<form action=\"/transfer/" + eventInfo.getId() + "/" + resultSet.getString("ticket_type") + "\" method=\"post\">\n" +
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
