package Login;

import ServerFramework.TicketServerConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
        req.getSession().invalidate();
        resp.getWriter().println(TicketServerConstants.PAGE_HEADER);
        resp.getWriter().println("<h1>Logout successful.</h1>");
        resp.getWriter().println(TicketServerConstants.PAGE_FOOTER);

    }
}
