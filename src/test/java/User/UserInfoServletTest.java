package User;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserInfoServletTest {

    @Test
    public void testInsertedCorrectly() {
        try {
            String expected = "expected";
            List<String> data = new ArrayList<>();

            // Use Mockito library to create mocked request and response
            HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
            HttpServletResponse mockedResponse = mock(HttpServletResponse.class);
            // We also need a mocked context to return the data structure
            ServletContext context = mock(ServletContext.class);

            // set up the mocked request to return a correct parameter value
            when(mockedRequest.getParameter("message")).thenReturn(expected);
            // set the mocked request to return the mocked context
            when(mockedRequest.getServletContext()).thenReturn(context);
            // set up the mocked context to return the data structure
            when(context.getAttribute("data")).thenReturn(data);

            // execute the doGet and verify that the method
            // added the expected value to the data structure
            UserInfoServlet ts = new UserInfoServlet();
            ts.doGet(mockedRequest, mockedResponse);
            Assertions.assertEquals(data.get(0), expected);
        } catch (IOException | ServletException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBadParameterKey() {
        try {
            List<String> data = new ArrayList<>();

            // Use Mockito library to create mocked request and response
            HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
            HttpServletResponse mockedResponse = mock(HttpServletResponse.class);
            // We also need a mocked context to return the data structure
            ServletContext context = mock(ServletContext.class);

            // set up the mocked request to return a correct parameter value
            when(mockedRequest.getParameter("message")).thenReturn(null);
            // set the mocked request to return the mocked context
            when(mockedRequest.getServletContext()).thenReturn(context);
            // set up the mocked context to return the data structure
            when(context.getAttribute("data")).thenReturn(data);

            // Execute the mocked request
            UserInfoServlet ts = new UserInfoServlet();
            ts.doGet(mockedRequest, mockedResponse);
            // The mocked request returned null for the parameter value
            // so no data should have been added to the data structure.
            Assertions.assertEquals(data.size(), 0);

        } catch (IOException | ServletException e) {
            fail(e.getMessage());
        }
    }
}