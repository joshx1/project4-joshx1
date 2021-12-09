package APIs;

import ConnectionPool.DBCPDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;
import utilities.DBUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class PurchaseTicketServlet extends HttpServlet {
    ClientInfo clientInfo;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] URI = req.getRequestURI().split("/");
        System.out.println(Arrays.toString(URI));
        try (BufferedReader reader = req.getReader()) {
            String body = URLDecoder.decode(reader.readLine(), StandardCharsets.UTF_8.toString());
            String [] bodyParts = body.split("=");
            String eventId = bodyParts[1];
            //TODO: verify the body exists and it contains a =
            System.out.println("body: " + body);
            String email = URI[3];
            Connection connection = DBCPDataSource.getConnection();
            clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            DBUtilities.buyTicket(connection, email, eventId);
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
