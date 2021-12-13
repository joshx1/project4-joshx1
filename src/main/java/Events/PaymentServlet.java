package Events;

import ConnectionPool.DBCPDataSource;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpStatus;
import utilities.DBUtilitiesClient;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import utilities.DBUtilitiesTicketing;

import static utilities.VerifyAuthenticated.checkAuthentication;

public class PaymentServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();
        //try {
        //    Connection connection = DBCPDataSource.getConnection();
        //    String email = DBUtilities.emailFromSessionId(connection, sessionId);
        //    ClientInfo clientInfo = DBUtilities.userInfoFromEmail(connection, email);
        //} catch (SQLException throwables) {
        //    throwables.printStackTrace();
        //}

        if (checkAuthentication(req, resp, sessionId)) return;
        String[] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        System.out.println(Arrays.toString(bodyParts));
        System.out.println(Arrays.toString(URI));
        String ticketType = URI[3];
        String eventId = URI[2];
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
            if (!DBUtilitiesTicketing.checkIfEventFull(connection, Integer.parseInt(eventId))) {
                resp.setStatus(HttpStatus.OK_200);
                resp.getWriter().println("<h1> Event is sold out. </h1>");
                resp.getWriter().println("<form action=\"/login" + "\" method=\"get\">" +
                    "<button name=\"returnhome\" value=" + ">Return to home</button>" +
                    "</form>");
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        resp.getWriter().println("<h1>Do you have enough money to purchase this event?</h1>");
        resp.getWriter().println("<form action=\"/purchase/" + eventId + "\" method=\"post\">" +
            "<button name=\"type\" value=" + ticketType + ">Yes</button>" +
            "</form>");
        resp.getWriter().println("<form action=\"/login\" method=\"get\">" +
            "<button name=\"type\" value=>No</button>" +
            "</form>");
        return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        System.out.println(Arrays.toString(bodyParts));
        System.out.println(Arrays.toString(URI));
        String ticketType = bodyParts[1];
        Integer eventId = Integer.parseInt(URI[2]);
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
            //ClientInfo clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            DBUtilitiesTicketing.buyTicket(connection, email, eventId, ticketType);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            resp.getWriter().println(TicketServerConstants.ERROR + TicketServerConstants.RETURN_HOME);
            return;
        }
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(TicketServerConstants.SUCCESS + TicketServerConstants.RETURN_HOME);
        return;
    }
}
