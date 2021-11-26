package UserInfo;

import ConnectionPool.DBCPDataSource;
import ConnectionPool.DBUtilities;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;
import utilities.Config;
import utilities.HTTPFetcher;
import utilities.LoginUtilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Implements logic for the /login path where Slack will redirect requests after
 * the user has entered their auth info.
 */
public class UserInfoServlet extends HttpServlet {

    private ClientInfo clientInfo;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilities.emailFromSessionId(connection, sessionId);
            ClientInfo clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            System.out.println(clientInfo.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
