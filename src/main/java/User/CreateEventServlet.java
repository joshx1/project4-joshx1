package User;

import ConnectionPool.DBCPDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.ClientInfo;
import utilities.DBUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import utilities.DBUtilitiesEvents;

import static utilities.VerifyAuthenticated.checkAuthentication;

public class CreateEventServlet extends HttpServlet {

    private ClientInfo clientInfo;

    String[] bodyParts;
    String[] bodies;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String sessionId = req.getSession(true).getId();
        if (checkAuthentication(req, resp, sessionId)) return;
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilities.emailFromSessionId(connection, sessionId);
            clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            resp.getWriter().println("<h1> Create Event </h1>");
            resp.getWriter().println("<form action=\"/createevent/"+ clientInfo.getEmail() + "\" method=\"post\">\n" +
                "  <label for=\"name\">Event name:</label><br/>\n" +
                "  <input type=\"text\" id=\"name\" name=\"name\" required/><br/>\n" +
                "  <label for=\"date\">Event date (YYYY-MM-DD):</label><br/>\n" +
                "  <input type=\"date\" id=\"date\" name=\"date\" required/><br/>\n" +
                "  <label for=\"location\">Location:</label><br/>\n" +
                "  <input type=\"text\" id=\"location\" name=\"location\" required/><br/>\n" +
                "  <label for=\"price\">Standard price:</label><br/>\n" +
                "  <input type=\"number\" id=\"price\" name=\"price\" required/><br/>\n" +
                "  <label for=\"priceStudent\">Student price:</label><br/>\n" +
                "  <input type=\"number\" id=\"priceStudent\" name=\"priceStudent\" required/><br/>\n" +
                "  <label for=\"priceVIP\">VIP price:</label><br/>\n" +
                "  <input type=\"number\" id=\"priceVIP\" name=\"priceVIP\" required/><br/>\n" +
                "  <label for=\"capacity\">Capacity:</label><br/>\n" +
                "  <input type=\"number\" id=\"capacity\" name=\"capacity\" required/><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String sessionId = req.getSession(true).getId();
        if (checkAuthentication(req, resp, sessionId)) return;
        String[] URI = req.getRequestURI().split("/");
        // Process the request body.
        try (BufferedReader reader = req.getReader()) {
            String body = URLDecoder.decode(reader.readLine(), StandardCharsets.UTF_8.toString());
            //TODO: verify the body exists and it contains a =
            System.out.println("body: " + body);
            String name = null;
            String location = null;
            Date date = null;
            int capacity = 0;
            float price = 0;
            // Issue how to initialize as null?
            float priceStudent = 0;
            float priceVIP = 0;
            bodies = body.split("&");
            System.out.println(bodies);
            for (int i = 0; i < bodies.length; i++) {
                bodyParts = bodies[i].split("=");
                System.out.println(bodyParts[0]);
                if (bodyParts[0].startsWith("name") && bodyParts.length == 2) {
                    System.out.println(bodyParts[0]);
                    name = bodyParts[1];
                } else if (bodyParts[0].startsWith("location") && bodyParts.length == 2) {
                    System.out.println(bodyParts[0]);
                    location = bodyParts[1];
                } else if (bodyParts[0].startsWith("date") && bodyParts.length == 2) {
                    System.out.println(bodyParts[0]);
                    System.out.println(bodyParts[1]);
                    date = Date.valueOf(bodyParts[1]);
                } else if (bodyParts[0].startsWith("price") && bodyParts.length == 2) {
                    System.out.println(bodyParts[0]);
                    price = Float.parseFloat(bodyParts[1]);
                } else if (bodyParts[0].startsWith("priceStudent") && bodyParts.length == 2) {
                    System.out.println(bodyParts[0]);
                    priceStudent = Float.parseFloat(bodyParts[1]);
                } else if (bodyParts[0].startsWith("priceVIP") && bodyParts.length == 2) {
                    System.out.println(bodyParts[0]);
                    priceVIP = Float.parseFloat(bodyParts[1]);
                } else if (bodyParts[0].startsWith("capacity") && bodyParts.length == 2) {
                    System.out.println(bodyParts[0]);
                    priceVIP = Integer.parseInt(bodyParts[1]);
                }
            }
            Connection connection = DBCPDataSource.getConnection();
            DBUtilitiesEvents.executeInsertEvent(connection, URI[2], name, location, date, price, priceStudent, priceVIP, capacity);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        resp.getWriter().println("<h1> Update success! </h1>");
        resp.getWriter().println("<form action=\"/login" + "\" method=\"get\">" +
            "<button name=\"returnhome\" value=" + ">Return to home</button>" +
            "</form>");
    }
}
