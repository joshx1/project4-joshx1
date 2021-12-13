package User;

import ConnectionPool.DBCPDataSource;
import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpStatus;
import utilities.DBUtilitiesClient;
import utilities.DBUtilitiesTicketing;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import static utilities.VerifyAuthenticated.checkAuthentication;

/**
 * Handles the transfer ticket functionality.
 */
public class TransferTicketServlet extends HttpServlet {

    /**
     * Given a request, this will transfer the ticket from one user to another.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String sessionId = req.getSession(true).getId();
        if (checkAuthentication(req, resp, sessionId)) return;
        String[] URI = req.getRequestURI().split("/");
        req.getQueryString();
        String query = IOUtils.toString(req.getInputStream(), "UTF-8");
        String[] bodyParts = query.split("=");
        System.out.println(Arrays.toString(bodyParts));
        System.out.println(Arrays.toString(URI));
        String emailReceiver = bodyParts[1];
        int eventId = Integer.parseInt(URI[2]);
        String ticketType = URI[3];
        try {
            Connection connection = DBCPDataSource.getConnection();
            String emailSender = DBUtilitiesClient.emailFromSessionId(connection, sessionId);
            //ClientInfo clientInfo = DBUtilities.userInfoFromEmail(connection, email);
            DBUtilitiesTicketing.transferTicket(connection, emailSender, emailReceiver, eventId, ticketType);
        } catch (SQLException throwables) {
            resp.setStatus(HttpStatus.BAD_REQUEST_400);
            resp.getWriter().println(TicketServerConstants.ERROR + TicketServerConstants.RETURN_HOME);
            throwables.printStackTrace();
            return;
        }
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(TicketServerConstants.SUCCESS + TicketServerConstants.RETURN_HOME);
        return;
    }
}
