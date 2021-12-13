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

/**
 * This class is a stand in payment processor.
 */
public class PaymentServlet extends HttpServlet {

    /**
     * This returns a page asking the user if they have enough money to afford the event.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();

        if (checkAuthentication(req, resp, sessionId)) return;
        String[] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        String ticketType = URI[3];
        String eventId = URI[2];
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
            if (!DBUtilitiesTicketing.checkIfEventFull(connection, Integer.parseInt(eventId))) {
                resp.setStatus(HttpStatus.OK_200);
                resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
                resp.getWriter().println("<h1> Event is sold out. </h1>");
                resp.getWriter().println("<form action=\"/login" + "\" method=\"get\">" +
                    "<button name=\"returnhome\" value=" + ">Return to home</button>" +
                    "</form>");
                resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
                return;
            }
        } catch (SQLException throwables) {
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println(TicketServerConstants.ERROR + TicketServerConstants.RETURN_HOME);
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
            return;
        }
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
        resp.getWriter().println("<h1>Do you have enough money to purchase this event?</h1>");
        resp.getWriter().println("<form action=\"/purchase/" + eventId + "\" method=\"post\">" +
            "<button name=\"type\" value=" + ticketType + ">Yes</button>" +
            "</form>");
        resp.getWriter().println("<form action=\"/login\" method=\"get\">" +
            "<button name=\"type\" value=>No</button>" +
            "</form>");
        resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        return;
    }

    /**
     * This post takes in the request sent by the payment page.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        String ticketType = bodyParts[1];
        Integer eventId = Integer.parseInt(URI[2]);
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
            DBUtilitiesTicketing.buyTicket(connection, email, eventId, ticketType);
        } catch (SQLException throwables) {
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println(TicketServerConstants.ERROR + TicketServerConstants.RETURN_HOME);
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
            return;
        }
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
        resp.getWriter().println(TicketServerConstants.SUCCESS + TicketServerConstants.RETURN_HOME);
        resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        return;
    }
}
