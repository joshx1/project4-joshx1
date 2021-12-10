package User;

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

public class TransferTicketServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        System.out.println(Arrays.toString(bodyParts));
        System.out.println(Arrays.toString(URI));
        String emailReceiver = bodyParts[1];
        int eventId = Integer.parseInt(URI[2]);
        String ticketType = URI[3];
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        try {
            Connection connection = DBCPDataSource.getConnection();
            String emailSender = DBUtilities.emailFromSessionId(connection, sessionId);
            //ClientInfo clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            DBUtilities.transferTicket(connection, emailSender, emailReceiver, eventId, ticketType);
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
