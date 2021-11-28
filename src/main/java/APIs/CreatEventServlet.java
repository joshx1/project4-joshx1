package APIs;

import ConnectionPool.DBCPDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.DBUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class CreatEventServlet extends HttpServlet {

    String[] bodyParts;
    String[] bodies;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] URI = req.getRequestURI().split("/");
        // Process the request body.
        try (BufferedReader reader = req.getReader()) {
            String body = URLDecoder.decode(reader.readLine(), StandardCharsets.UTF_8.toString());
            //TODO: verify the body exists and it contains a =
            System.out.println("body: " + body);
            String name = null;
            String location = null;
            Date date = null;
            float price = 0;
            // Issue how to initialize as null?
            float priceStudent = 0;
            float priceVIP = 0;
            bodies = body.split("&");
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
                }
            }

            Connection connection = DBCPDataSource.getConnection();
            DBUtilities.executeInsertEvent(connection, URI[3], name, location, date, price, priceStudent, priceVIP);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
