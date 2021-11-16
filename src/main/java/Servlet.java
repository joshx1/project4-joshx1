package example.echo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Handle requests to /echo
 */
public class Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println(EchoServerConstants.GET_ECHO_PAGE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Retrieve the shared data structure from the context.
        List<String> messages = (List<String>)req.getSession().getServletContext().getAttribute(EchoServer.MSG_KEY);

        // Process the request body.
        try(BufferedReader reader = req.getReader()) {
            String body = URLDecoder.decode(reader.readLine(), StandardCharsets.UTF_8.toString());
            //TODO: verify the body exists and it contains a =
            System.out.println("body: " + body);
            String[] bodyParts = body.split("=");
            String message = bodyParts[1];
            messages.add(message);
        }

        // Send response
        PrintWriter writer = resp.getWriter();
        resp.setStatus(HttpStatus.OK_200);
        resp.setStatus(HttpStatus.OK_200);
        writer.println(EchoServerConstants.PAGE_HEADER);
        writer.println("<h3>Messages</h3>\n");
        writer.println("<ul>\n");
        for(String message: messages) {
            writer.println("<li>" + message + "</li>");
        }
        writer.println("</ul>\n");
        writer.println(EchoServerConstants.PAGE_FOOTER);

    }
}
