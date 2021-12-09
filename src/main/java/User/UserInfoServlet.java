package User;

import ConnectionPool.DBCPDataSource;
import utilities.DBUtilities;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import utilities.ClientInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
            clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            System.out.println(clientInfo.getName());
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1> User information </h1>");
            resp.getWriter().println("<h1>Name: " + clientInfo.getName() + "</h1>");
            resp.getWriter().println("<h1>Location: " + clientInfo.getLocation() + "</h1>");
            //resp.getWriter().println("<iframe name=\"dummyframe\" id=\"dummyframe\" style=\"display: none;\"></iframe>");
            resp.getWriter().println("<form action=\"/api/userinformation/"+ clientInfo.getEmail() + "\" method=\"post\">\n" +
                "  <label for=\"msg\">New location:</label><br/>\n" +
                "  <input type=\"text\" id=\"location\" name=\"location\"/><br/>\n" +
                //"  <input onclick=\"window.location.href = '/userinfo'\" />" +
                "  <input type=\"submit\" value=\"Submit\"/>\n" +
                "</form>");
            resp.getWriter().println("<p><a href=\"/api/userinformation/" + clientInfo.getEmail() + "\">Update user information</a>");
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
