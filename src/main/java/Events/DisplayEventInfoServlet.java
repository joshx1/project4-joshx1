package Events;

import ConnectionPool.DBCPDataSource;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;
import utilities.DBUtilities;
import utilities.EventInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            resp.getWriter().println("<h1>Capacity: " + eventInfo.getCapacity() + "</h1>");
            resp.getWriter().println("<h1>Price: " + eventInfo.getPrice() + "</h1>");
            resp.getWriter().println("<h1>Student Price: " + eventInfo.getPriceStudent() + "</h1>");
            resp.getWriter().println("<h1>VIP Price: " + eventInfo.getPriceVIP() + "</h1>");
            resp.getWriter().println("<form action=\"/api/buy/" + clientInfo.getEmail() + "\" method=\"post\">" +
            "<button name=\"eventid\" value=" + eventInfo.getId() + ">Buy ticket</button>" +
            "</form>");

            resp.getWriter().println("<form action=\"/api/buy/" + clientInfo.getEmail() + "\" method=\"post\">" +
                "<button name=\"eventid\" value=" + eventInfo.getId() + ">Buy ticket</button>" +
                "</form>");

            resp.getWriter().println("<form action=\"/api/buy/" + clientInfo.getEmail() + "\" method=\"post\">" +
                "<button name=\"eventid\" value=" + eventInfo.getId() + ">Buy ticket</button>" +
                "</form>");
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
