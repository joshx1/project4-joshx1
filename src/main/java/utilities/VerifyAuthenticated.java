package utilities;

import ConnectionPool.DBCPDataSource;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class VerifyAuthenticated {
    public static boolean checkAuthentication(HttpServletRequest req, HttpServletResponse resp, String sessionId) throws IOException {
        try {
            Connection connection = DBCPDataSource.getConnection();
            String email = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            req.getSession().setAttribute(TicketServerConstants.CLIENT_INFO_KEY, null);
            resp.setStatus(HttpStatus.UNAUTHORIZED_401);
            resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>Failed to retrieve information.</h1>");
            resp.getWriter().println("<form action=\"/" + "\" method=\"get\">" +
                "<button name=\"returnhome\" value=" + ">Authentication error, please login again.</button>" +
                "</form>");
            resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);
            return true;
        }
        return false;
    }
}
