package utilities;

import utilities.ClientInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to demonstrate how to connect to a MySQL database using JDBC.
 * Some code taken from examples here: https://www.baeldung.com/java-jdbc
 * Also see https://www.baeldung.com/java-connection-pooling
 */
public class DBUtilities {

    /**
     * A method to demonstrate using a PreparedStatement to execute a database insert.
     */
    public static void executeInsertSessionData(Connection con, String sessionId, String email) throws SQLException {
        String insertContactSql = "INSERT INTO SessionData (sessionId, email) VALUES (?, ?);";
        PreparedStatement insertContactStmt = con.prepareStatement(insertContactSql);
        insertContactStmt.setString(1, sessionId);
        insertContactStmt.setString(2, email);
        insertContactStmt.executeUpdate();
    }

    public static void executeInsertClientData(Connection con, String name, String access_token, String token_type, String id_token, String email, boolean email_verified) throws SQLException {
        String insertContactSql = "INSERT INTO ClientData (name, access_token, token_type, id_token, email, email_verified) VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement insertContactStmt = con.prepareStatement(insertContactSql);
        insertContactStmt.setString(1, name);
        insertContactStmt.setString(2, access_token);
        insertContactStmt.setString(3, token_type);
        insertContactStmt.setString(4, id_token);
        insertContactStmt.setString(5, email);
        insertContactStmt.setBoolean(6, email_verified);
        insertContactStmt.executeUpdate();
    }

    /**
     * Updates client's location.
     * @param con
     * @param email
     * @param location
     * @throws SQLException
     */
    public static void executeInsertClientLocation(Connection con, String email, String location) throws SQLException {
        String inserClientLocationSql = "UPDATE ClientData SET location = ? WHERE email = ?;";
        PreparedStatement insertClientLocationStmt = con.prepareStatement(inserClientLocationSql);
        insertClientLocationStmt.setString(1, location);
        insertClientLocationStmt.setString(2, email);
        insertClientLocationStmt.executeUpdate();
    }


    /**
     * Updates event's location.
     * @param con
     * @param event_id
     * @param location
     * @throws SQLException
     */
    public static void executeInsertEventLocation(Connection con, int event_id, String location) throws SQLException {
        String inserClientLocationSql = "UPDATE EventsData SET location = ? WHERE id = ?;";
        PreparedStatement insertClientLocationStmt = con.prepareStatement(inserClientLocationSql);
        insertClientLocationStmt.setString(1, location);
        insertClientLocationStmt.setInt(2, event_id);
        insertClientLocationStmt.executeUpdate();
    }

    /**
     * Updates client's DOB.
      * @param con
     * @param email
     * @param dob
     * @throws SQLException
     */
    public static void executeInsertClientDOB(Connection con, String email, Date dob) throws SQLException {
        String insertClientDOBSql = "UPDATE ClientData SET dob = ? WHERE email = ?;";
        PreparedStatement insertClientDOBStmt = con.prepareStatement(insertClientDOBSql);
        insertClientDOBStmt.setDate(1, dob);
        insertClientDOBStmt.setString(2, email);
        insertClientDOBStmt.executeUpdate();
    }

    public static void executeInsertClientName(Connection con, String email, String name) throws SQLException {
        String insertClientNameSql = "UPDATE ClientData SET name = ? WHERE email = ?;";
        PreparedStatement insertClientNameStmt = con.prepareStatement(insertClientNameSql);
        insertClientNameStmt.setString(1, name);
        insertClientNameStmt.setString(2, email);
        insertClientNameStmt.executeUpdate();
    }

    public static int checkClientExists(Connection con, String email) throws SQLException {
        String checkClientExist = "SELECT COUNT(1) FROM ClientData WHERE email = ?;";
        PreparedStatement checkClientStmt = con.prepareStatement(checkClientExist);
        checkClientStmt.setString(1, email);
        ResultSet results  = checkClientStmt.executeQuery();
        results.next();
        return results.getInt(1);
    }

    /**
     * Returns email for a given session ID.
     * @param con
     * @throws SQLException
     */
    public static String emailFromSessionId(Connection con, String sessionId) throws SQLException {
        String selectSessionId = "SELECT * FROM SessionData WHERE sessionId = ?;";
        PreparedStatement selectSessionIdStmt = con.prepareStatement(selectSessionId);
        selectSessionIdStmt.setString(1, sessionId);
        ResultSet results = selectSessionIdStmt.executeQuery();
        results.next();
        return results.getString("email");
    }

    /**
     * Returns user information given user email.
     * @param con
     * @throws SQLException
     */
    public static utilities.ClientInfo userInfoFromEmail(Connection con, String email) throws SQLException {
        String selectClientInfo = "SELECT * FROM ClientData WHERE email = ?;";
        PreparedStatement selectClientInfoStmt = con.prepareStatement(selectClientInfo);
        selectClientInfoStmt.setString(1, email);
        ResultSet results = selectClientInfoStmt.executeQuery();
        results.next();
        ClientInfo clientInfo = new ClientInfo(results.getString("name"),
            results.getString("access_token"),
            results.getString("token_type"),
            results.getString("id_token"),
            results.getString("email"),
            results.getBoolean("email_verified"),
            results.getString("location"));
        return clientInfo;
    }

    public static void executeInsertEvent(Connection con, String email, String name, String location, Date date, int capacity, float price, float priceStudent, float priceVIP, byte[] image) throws SQLException {
        String insertEventSql = "INSERT INTO EventsData (creator, name, date, location, capacity, price, price_student, price_VIP, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement insertClientLocationStmt = con.prepareStatement(insertEventSql);
        insertClientLocationStmt.setString(1, email);
        insertClientLocationStmt.setString(2, name);
        insertClientLocationStmt.setDate(3, date);
        insertClientLocationStmt.setString(4, location);
        insertClientLocationStmt.setInt(5, capacity);
        insertClientLocationStmt.setDouble(6, price);
        insertClientLocationStmt.setDouble(7, priceStudent);
        insertClientLocationStmt.setDouble(8, priceVIP);
        insertClientLocationStmt.setBytes(9, image);
        insertClientLocationStmt.executeUpdate();
    }

    /**
     * Get details for all events.
     * @param con
     * @throws SQLException
     */
    public static ResultSet executeSelectEvents(Connection con) throws SQLException {
        String selectAllContactsSql = "SELECT * FROM EventsData;";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(selectAllContactsSql);
        ResultSet results = selectAllContactsStmt.executeQuery();
        return results;
        }

    /**
     * Get for events by name, partial matches are returned.
     * @param con
     * @throws SQLException
     */
    public static ResultSet searchEvents(Connection con, String query) throws SQLException {
        System.out.println("DB");
        System.out.println(query);
        String searchSql = "SELECT * FROM EventsData WHERE name LIKE ?;";
        PreparedStatement searchStmt = con.prepareStatement(searchSql);
        searchStmt.setString(1,'%' + query + '%');
        ResultSet results = searchStmt.executeQuery();
        return results;
    }

    /**
     * Get details for a specific event.
     * @param con
     * @throws SQLException
     */
    public static EventInfo executeSelectSpecificEvent(Connection con, int id) throws SQLException {
        String selectSql = "SELECT * FROM EventsData WHERE id = ?;";
        PreparedStatement selectClientInfoStmt = con.prepareStatement(selectSql);
        selectClientInfoStmt.setInt(1, id);
        ResultSet results = selectClientInfoStmt.executeQuery();
        results.next();
        EventInfo eventInfo = new EventInfo(
            results.getInt("id"),
            results.getString("creator"),
            results.getString("name"),
            results.getString("location"),
            results.getFloat("price"),
            results.getFloat("price_VIP"),
            results.getFloat("price_Student"),
            results.getDate("date")
            );
        return eventInfo;
    }

    /**
     * Returns all events user purchased.
     * @param con
     * @param email
     * @return
     * @throws SQLException
     */
    public static ResultSet eventsPurchased(Connection con, String email) throws SQLException {
        String purchasedEventsSql = "SELECT * FROM EventsData WHERE id = (SELECT event_id FROM EventsAndGuests WHERE email = ?);";
        PreparedStatement purchasedEventsStmt = con.prepareStatement(purchasedEventsSql);
        purchasedEventsStmt.setString(1,email);
        ResultSet results = purchasedEventsStmt.executeQuery();
        return results;
    }

    /**
     * Returns the events created by the owner of the email.
     * @param con
     * @param email
     * @return
     * @throws SQLException
     */
    public static ResultSet eventsCreated(Connection con, String email) throws SQLException {
        String purchasedEventsSql = "SELECT * FROM EventsData WHERE email = ?);";
        PreparedStatement purchasedEventsStmt = con.prepareStatement(purchasedEventsSql);
        purchasedEventsStmt.setString(1,email);
        ResultSet results = purchasedEventsStmt.executeQuery();
        return results;
    }

    /**
     * Buys ticket from SQL database.
     * @param con
     * @param email
     * @param eventId
     * @throws SQLException
     */
    public static void buyTicket(Connection con, String email, String eventId, String ticketType) throws SQLException {
        String insertContactSql = "INSERT INTO EventsAndGuests (event_id, email, type) VALUES (?, ?, ?);";
        PreparedStatement insertContactStmt = con.prepareStatement(insertContactSql);
        insertContactStmt.setString(1, eventId);
        insertContactStmt.setString(2, email);
        insertContactStmt.setString(3, ticketType);
        insertContactStmt.executeUpdate();
        String increaseTicketCountSql = "UPDATE EventsAndGuests SET amount = amount + 1 WHERE email = ? AND type = ?;";
        PreparedStatement increaseTicketCountStmt = con.prepareStatement(increaseTicketCountSql);
        increaseTicketCountStmt.setString(1, email);
        increaseTicketCountStmt.setString(2, ticketType);
    }

    /**
     * A method to demonstrate using a PrepareStatement to execute a database select
     * @param con
     * @throws SQLException
     */
    public static void executeSelectAndPrint(Connection con) throws SQLException {
        String selectAllContactsSql = "SELECT * FROM contacts;";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(selectAllContactsSql);
        ResultSet results = selectAllContactsStmt.executeQuery();
        while(results.next()) {
            System.out.printf("Name: %s\n", results.getString("name"));
            System.out.printf("Extension: %s\n", results.getInt("extension"));
            System.out.printf("Email: %s\n", results.getString("email"));
            System.out.printf("Start Date: %s\n", results.getString("startdate"));
        }
    }
}
