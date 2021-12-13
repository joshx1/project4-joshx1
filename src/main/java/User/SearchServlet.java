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
import org.apache.commons.io.IOUtils;
import utilities.DBUtilitiesEvents;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Arrays;

import static utilities.VerifyAuthenticated.checkAuthentication;

/**
 * This class handles all search event responsibilities.
 */
public class SearchServlet extends HttpServlet {

    /**
     * Allows the user to search for events by name.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();
        if (checkAuthentication(req, resp, sessionId)) return;
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println(TicketServerConstants.ERROR + TicketServerConstants.RETURN_HOME);
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
            return;
        }
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
        resp.getWriter().println("<h1> Event Search </h1>");
        resp.getWriter().println("<form action=\"/search\" method=\"post\">\n" +
            "  <label for=\"queryName\">Search event name:</label><br/>\n" +
            "  <input type=\"text\" id=\"queryName\" name=\"queryName\"/><br/>\n" +
            "  <input type=\"submit\" value=\"Submit\"/>\n" +
            "</form>");
        resp.getWriter().println("<form action=\"/search\" method=\"post\">\n" +
            "  <label for=\"queryLocation\">Search event location:</label><br/>\n" +
            "  <input type=\"text\" id=\"queryLocation\" name=\"queryLocation\"/><br/>\n" +
            "  <input type=\"submit\" value=\"Submit\"/>\n" +
            "</form>");

        resp.getWriter().println(TicketServerConstants.RETURN_HOME);
        resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
    }

    /**
     * Returns all events searched for by user.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String sessionId = req.getSession(true).getId();
        if (checkAuthentication(req, resp, sessionId)) return;
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream());
        String[] queryList = query.split("=");
        System.out.println(Arrays.toString(queryList));
        String queryValue = queryList[1];
        String searchType = queryList[0];
        ResultSet results = null;
        try {
            Connection connection = DBCPDataSource.getConnection();
            if (searchType.equals("queryName")) {
                results = DBUtilitiesEvents.searchEventsName(connection, queryValue);
            } else if (searchType.equals("queryLocation")) {
                results = DBUtilitiesEvents.searchEventsLocation(connection, queryValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println(TicketServerConstants.ERROR + TicketServerConstants.RETURN_HOME);
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
            return;
        }
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
        resp.getWriter().println("<h1> All events searched for </h1>");
        try {
            while (results.next()) {
                resp.getWriter().println("<h1>Name: " + URLDecoder.decode(results.getString("name"), "UTF-8") + "</h1>");
                resp.getWriter().println("<p><a href=\"/event/" + results.getInt("id") + "\">" + "Event details</a>");
            }
            resp.getWriter().println(TicketServerConstants.RETURN_HOME);
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return;
    }
}
