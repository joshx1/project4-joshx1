package Events;

import ConnectionPool.DBCPDataSource;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;
import utilities.DBUtilitiesClient;
import utilities.DBUtilitiesEvents;
import utilities.EventInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import static utilities.VerifyAuthenticated.checkAuthentication;

/**
 * This class displays all information relevant to a specific event.
 */
public class DisplayEventInfoServlet extends HttpServlet {
    ClientInfo clientInfo;

    /**
     * This method displays all information to a specific event.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        if (checkAuthentication(req, resp, sessionId)) return;
        String [] URI = req.getRequestURI().split("/");
        System.out.println(URI.toString());
        int eventId = Integer.parseInt(URI[2]);
        System.out.println(URI[2]);
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
            clientInfo = DBUtilitiesClient.userInfoFromEmail(connection, email);
            EventInfo eventInfo = DBUtilitiesEvents.executeSelectSpecificEvent(connection, eventId);
            System.out.println(eventInfo.getName());
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1> User information </h1>");
            resp.getWriter().println("<h1>Name: " + eventInfo.getName() + "</h1>");
            resp.getWriter().println("<h1>Location: " + eventInfo.getLocation() + "</h1>");
            resp.getWriter().println("<h1>Price: " + eventInfo.getPrice() + "</h1>");
            resp.getWriter().println("<form action=\"/purchase/" + eventInfo.getId() + "/standard" + "\" method=\"get\">" +
                "<button name=\"type\" value=standard>Buy ticket</button>" +
                "</form>");

            resp.getWriter().println("<h1>Student Price: " + eventInfo.getPriceStudent() + "</h1>");
            resp.getWriter().println("<form action=\"/purchase/" + eventInfo.getId() + "/student" + "\" method=\"get\">" +
                "<button name=\"type\" value=student>Buy ticket</button>" +
                "</form>");

            resp.getWriter().println("<h1>VIP Price: " + eventInfo.getPriceVIP() + "</h1>");
            resp.getWriter().println("<form action=\"/purchase/" + eventInfo.getId() + "/VIP" + "\" method=\"get\">" +
                "<button name=\"type\" value=VIP>Buy ticket</button>" +
                "</form>");
            if (eventInfo.getCreator().equals(email)) {
                resp.getWriter().println("<h1>Edit event</h1>");
                resp.getWriter().println("<form action=\"/event/" + eventInfo.getId() + "\" method=\"post\">\n" +
                    "  <label for=\"name\">New name:</label><br/>\n" +
                    "  <input type=\"name\" id=\"name\" name=\"name\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>");
                resp.getWriter().println("<form action=\"/event/" + eventInfo.getId() + "\" method=\"post\">" +
                    "  <label for=\"msg\">New location:</label><br/>\n" +
                    "  <input type=\"text\" id=\"location\" name=\"location\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>");

                resp.getWriter().println("<form action=\"/event/" + eventInfo.getId() + "\" method=\"post\">" +
                    "  <label for=\"msg\">New date:</label><br/>\n" +
                    "  <input type=\"date\" id=\"date\" name=\"date\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>");

                resp.getWriter().println("<form action=\"/event/" + eventInfo.getId() + "\" method=\"post\">" +
                    "  <label for=\"msg\">New standard price:</label><br/>\n" +
                    "  <input type=\"price\" id=\"price\" name=\"price\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>");

                resp.getWriter().println("<form action=\"/event/" + eventInfo.getId() + "\" method=\"post\">" +
                    "  <label for=\"msg\">New student price:</label><br/>\n" +
                    "  <input type=\"priceStudent\" id=\"priceStudent\" name=\"priceStudent\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>");

                resp.getWriter().println("<form action=\"/event/" + eventInfo.getId() + "\" method=\"post\">" +
                    "  <label for=\"msg\">New VIP price:</label><br/>\n" +
                    "  <input type=\"priceVIP\" id=\"priceVIP\" name=\"priceVIP\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>");
                resp.getWriter().println("<h1>Delete event</h1>");
                resp.getWriter().println("<form action=\"/event/" + eventInfo.getId() + "\" method=\"post\">" +
                    "<button name=\"type\" value=delete>Delete event</button>" +
                    "</form>");
            }
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * This method updates the information of the current specific event.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String [] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        System.out.println(Arrays.toString(bodyParts));
        System.out.println(Arrays.toString(URI));
        int eventId = Integer.parseInt(URI[2]);

        try {
            Connection connection = DBCPDataSource.getConnection();
            if (bodyParts[1].equals("delete")) {
                System.out.println(URI[2]);
                System.out.println("yes");
                DBUtilitiesEvents.executeDeleteEvent(connection, eventId);
            } else if (bodyParts[0].equals("name") && bodyParts.length == 2) {
                System.out.println(URI[2]);
                System.out.println(bodyParts[1]);
                DBUtilitiesEvents.executeInsertEventName(connection, eventId, bodyParts[1]);
            } else if (bodyParts[0].equals("location") && bodyParts.length == 2) {
                DBUtilitiesEvents.executeInsertEventLocation(connection, eventId, bodyParts[1]);
            } else if (bodyParts[0].equals("date") && bodyParts.length == 2) {
                DBUtilitiesEvents.executeInsertEventDate(connection, eventId, Date.valueOf(bodyParts[1]));
            } else if (bodyParts[0].equals("price") && bodyParts.length == 2) {
                DBUtilitiesEvents.executeInsertEventStandardPrice(connection, eventId, Float.parseFloat(bodyParts[1]));
            } else if (bodyParts[0].equals("priceStudent") && bodyParts.length == 2) {
                DBUtilitiesEvents.executeInsertEventStudentPrice(connection, eventId, Float.parseFloat(bodyParts[1]));
            } else if (bodyParts[0].equals("priceVIP") && bodyParts.length == 2) {
                DBUtilitiesEvents.executeInsertEventVIPPrice(connection, eventId, Float.parseFloat(bodyParts[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
            resp.getWriter().println(TicketServerConstants.ERROR + TicketServerConstants.RETURN_HOME);
            return;
        }
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(TicketServerConstants.SUCCESS + TicketServerConstants.RETURN_HOME);
        return;
    }
}
