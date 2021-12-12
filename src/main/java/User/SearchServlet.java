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
import org.apache.commons.io.IOUtils;
import utilities.DBUtilitiesEvents;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Arrays;

/**
 * This class handles all search event responsibilities.
 */
public class SearchServlet extends HttpServlet {
    ClientInfo clientInfo;

    /**
     * Allows the user to search for events by name.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilities.emailFromSessionId(connection, sessionId);
            clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            resp.getWriter().println("<h1> Event Search </h1>");
            resp.getWriter().println("<form action=\"/search\" method=\"post\">\n" +
                "  <label for=\"query\">Search event name:</label><br/>\n" +
                "  <input type=\"text\" id=\"query\" name=\"query\"/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Returns all events searched for by user.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream());
        String[] queryList = query.split("=");
        System.out.println(Arrays.toString(queryList));
        String queryValue = queryList[1];
        try {
            Connection connection = DBCPDataSource.getConnection();
            ResultSet results = DBUtilitiesEvents.searchEvents(connection, queryValue);
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1> All events searched for </h1>");
            while(results.next()) {
                resp.getWriter().println("<h1>Name: " + results.getString("name") + "</h1>");
                resp.getWriter().println("<p><a href=\"/event/" + results.getInt("id") + "\">" + results.getString("name") + "</a>");
            }
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
