package Login;

import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;

import static utilities.VerifyAuthenticated.checkAuthentication;

/**
 * Handles a request to sign out
 */
public class LogoutServlet extends HttpServlet {

    /**
     * This method logs the user out.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // log out by invalidating the session
        String sessionId = req.getSession(true).getId();
        if (checkAuthentication(req, resp, sessionId)) return;
        resp.setStatus(HttpStatus.OK_200);
        req.getSession().setAttribute(TicketServerConstants.CLIENT_INFO_KEY, null);
        req.getSession().invalidate();
        resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
        resp.getWriter().println("<h1>Logout successful.</h1>");
        resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);

    }
}
