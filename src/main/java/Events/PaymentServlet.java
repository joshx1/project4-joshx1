package Events;

import ConnectionPool.DBCPDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpStatus;
import utilities.DBUtilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import utilities.ClientInfo;

public class PaymentServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //String sessionId = req.getSession(true).getId();
        //try {
        //    Connection connection = DBCPDataSource.getConnection();
        //    String email = DBUtilities.emailFromSessionId(connection, sessionId);
        //    ClientInfo clientInfo = DBUtilities.userInfoFromEmail(connection, email);
        //} catch (SQLException throwables) {
        //    throwables.printStackTrace();
        //}
        String[] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        System.out.println(Arrays.toString(bodyParts));
        System.out.println(Arrays.toString(URI));
        String ticketType = URI[3];
        String eventId = URI[2];
        resp.getWriter().println("<h1>Do you have enough money to purchase this event?</h1>");
        resp.getWriter().println("<form action=\"/purchase/" + eventId + "\" method=\"post\">" +
            "<button name=\"type\" value=" + ticketType + ">Yes</button>" +
            "</form>");
        resp.getWriter().println("<form action=\"/login\" method=\"get\">" +
            "<button name=\"type\" value=>No</button>" +
            "</form>");
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
        String eventId = URI[2];
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilities.emailFromSessionId(connection, sessionId);
            //ClientInfo clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            DBUtilities.buyTicket(connection, email, eventId, ticketType);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println("<h1> Success </h1>");
        resp.getWriter().println("<form action=\"/login" + "\" method=\"get\">" +
            "<button name=\"returnhome\" value=" + ">Return to home</button>" +
            "</form>");
    }
}
