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
import utilities.DBUtilities;
import utilities.EventInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class DisplayEventInfoServlet extends HttpServlet {
    ClientInfo clientInfo;
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        String [] URI = req.getRequestURI().split("/");
        System.out.println(URI.toString());
        int eventId = Integer.parseInt(URI[2]);
        System.out.println(URI[2]);
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilities.emailFromSessionId(connection, sessionId);
            clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            EventInfo eventInfo = DBUtilities.executeSelectSpecificEvent(connection, eventId);
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
                resp.getWriter().println("<form action=\"/event/" + eventInfo.getId() + "\" method=\"get\">" +
                    "<button name=\"type\" value=VIP>Buy ticket</button>" +
                    "</form>");
                resp.getWriter().println("<h1>Location: " + eventInfo.getLocation() + "</h1>");
                resp.getWriter().println("<form action=\"/event/" + eventInfo.getId() + "\" method=\"post\">" +
                    "  <label for=\"msg\">New location:</label><br/>\n" +
                    "  <input type=\"text\" id=\"location\" name=\"location\"/><br/>\n" +
                    "  <input type=\"submit\" value=\"Submit\"/>\n" +
                    "</form>");
            }
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpStatus.OK_200);
        String [] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        System.out.println(Arrays.toString(bodyParts));
        System.out.println(Arrays.toString(URI));
        if (bodyParts[0].equals("name") && bodyParts.length == 2) {
            System.out.println(URI[2]);
            System.out.println(bodyParts[1]);
            try {
                Connection connection = DBCPDataSource.getConnection();
                DBUtilities.executeInsertClientName(connection, URI[2], bodyParts[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (bodyParts[0].equals("location") && bodyParts.length == 2) {
            System.out.println(bodyParts[1]);
            try {
                Connection connection = DBCPDataSource.getConnection();
                DBUtilities.executeInsertEventLocation(connection, Integer.parseInt(URI[2]), bodyParts[1]);
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
