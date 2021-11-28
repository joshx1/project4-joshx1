package APIs;
import ConnectionPool.DBCPDataSource;
import utilities.DBUtilities;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.net.URLDecoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

public class UpdateUserInformationServlet extends HttpServlet{
    String[] bodyParts;
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String [] URI = req.getRequestURI().split("/");
        // Process the request body.
        try(BufferedReader reader = req.getReader()) {
            String body = URLDecoder.decode(reader.readLine(), StandardCharsets.UTF_8.toString());
            //TODO: verify the body exists and it contains a =
            System.out.println("body: " + body);
            bodyParts = body.split("=");
        }
        if (bodyParts[0].equals("location") && bodyParts.length == 2) {
            System.out.println(URI[3]);
            System.out.println(bodyParts[1]);
            try {
                Connection connection = DBCPDataSource.getConnection();
                DBUtilities.executeInsertClientLocation(connection, URI[3], bodyParts[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
