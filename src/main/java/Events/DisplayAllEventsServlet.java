package Events;

import ConnectionPool.DBCPDataSource;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.DBUtilitiesEvents;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utilities.VerifyAuthenticated.checkAuthentication;

/**
 * This class handles the displaying of all events to the user.
 */
public class DisplayAllEventsServlet extends HttpServlet{

    /**
     * This method returns a page with all event names and links to them.
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
            ResultSet results = DBUtilitiesEvents.executeSelectEvents(connection);
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1> All events </h1>");
            while(results.next()) {
                resp.getWriter().println("<h1>Name: " + URLDecoder.decode(results.getString("name"), "UTF-8") + "</h1>");
                resp.getWriter().println("<p><a href=\"/event/" + results.getInt("id") + "\">" + "Event details</a>");
            }
            resp.getWriter().println(TicketServerConstants.RETURN_HOME);
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException e) {
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println(TicketServerConstants.ERROR);
            resp.getWriter().println(TicketServerConstants.RETURN_HOME);
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        }
    }
}
