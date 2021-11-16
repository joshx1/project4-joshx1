package ServerFramework;

/**
 * A helper class to maintain constants used for the ServerFramework.EchoServer example.
 */


public class EchoServerConstants {
    public static final String ECHO = "/echo";

    public static final String PAGE_HEADER = "<!DOCTYPE html>\n" +
        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
        "<head>\n" +
        "  <title>Echo</title>\n" +
        "</head>\n" +
        "<body>\n" +
        "\n";

    public static final String PAGE_FOOTER = "\n" +
        "</body>\n" +
        "</html>";

    public static final String GET_ECHO_PAGE = PAGE_HEADER +
        "<form action=\"/echo\" method=\"post\">\n" +
        "  <label for=\"msg\">Message:</label><br/>\n" +
        "  <input type=\"text\" id=\"msg\" name=\"msg\"/><br/>\n" +
        "  <input type=\"submit\" value=\"Submit\"/>\n" +
        "</form>" +
        PAGE_FOOTER;
}
