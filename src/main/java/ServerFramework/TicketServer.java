package ServerFramework;

//import APIs.PurchaseTicketServlet;
import Events.DisplayEventInfoServlet;
import Events.PaymentServlet;
import Login.LandingServlet;
import Login.LoginServlet;
import Login.LogoutServlet;
import Events.DisplayAllEventsServlet;
import User.*;
    import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import utilities.Config;

import java.io.FileReader;

/**
 * This program is an event ticketing website implemented using Jetty. It allows for the functionailty as specified in
 * the README file.
 * Author: Josh Li
 * Email: jxli2@dons.usfca.edu
 */

/**
 * This is the main class of the program and it handles starting up the server as well as setting up the servlets.
 */
public class TicketServer {

    public static final int PORT = 8080;
    private static final String configFilename = "config.json";

    public static void main(String[] args) {
        try {
            startup();
        } catch(Exception e) {
            // catch generic Exception as that is what is thrown by server start method
            e.printStackTrace();
        }
    }

    /**
     * A helper method to start the server.
     * @throws Exception -- generic Exception thrown by server start method
     */
    public static void startup() throws Exception {

        // read the client id and secret from a config file
        Gson gson = new Gson();
        Config config = gson.fromJson(new FileReader(configFilename), Config.class);

        // create a new server
        Server server = new Server(PORT);

        // make the config information available across servlets by setting an
        // attribute in the context
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setAttribute(TicketServerConstants.CONFIG_KEY, config);

        context.addServlet(LandingServlet.class, "/");
        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(UserInfoServlet.class, "/userinfo/*");
        context.addServlet(UserTransactionsServlet.class, "/usertransactions");
        context.addServlet(TransferTicketServlet.class, "/transfer/*");
        context.addServlet(DisplayAllEventsServlet.class, "/events");
        context.addServlet(DisplayEventInfoServlet.class, "/event/*");
        context.addServlet(PaymentServlet.class, "/purchase/*");
        context.addServlet(CreateEventInputServlet.class, "/createevent/*");
        context.addServlet(SearchServlet.class, "/search");
        context.addServlet(LogoutServlet.class, "/logout");

        // start it up!
        server.setHandler(context);
        server.start();
    }
}
