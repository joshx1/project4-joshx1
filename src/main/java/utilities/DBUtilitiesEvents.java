package utilities;

import java.sql.*;

/**
 * This class handles all the MySQL methods related to event information and creation.
 */
public class DBUtilitiesEvents {

    /**
     * Create new event in database.
     *
     * @param con
     * @param email
     * @param name
     * @param location
     * @param date
     * @param price
     * @param priceStudent
     * @param priceVIP
     * @throws SQLException
     */
    public static void executeInsertEvent(Connection con, String email, String name, String location, Date date, float price, float priceStudent, float priceVIP, int capacity) throws SQLException {
        String insertEventSql = "INSERT INTO EventsData (creator, name, date, location, price, price_student, price_VIP, capacity) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement insertClientLocationStmt = con.prepareStatement(insertEventSql);
        insertClientLocationStmt.setString(1, email);
        insertClientLocationStmt.setString(2, name);
        insertClientLocationStmt.setDate(3, date);
        insertClientLocationStmt.setString(4, location);
        insertClientLocationStmt.setDouble(5, price);
        insertClientLocationStmt.setDouble(6, priceStudent);
        insertClientLocationStmt.setDouble(7, priceVIP);
        insertClientLocationStmt.setDouble(8, capacity);
        insertClientLocationStmt.executeUpdate();
    }

    public static void executeDeleteEvent(Connection con, int eventId) throws SQLException {
        String deleteSql = "DELETE FROM EventsData WHERE id = ?;";
        PreparedStatement deleteStmt = con.prepareStatement(deleteSql);
        deleteStmt.setInt(1, eventId);
        deleteStmt.executeUpdate();
    }

    /**
     * Get details for all events.
     *
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
     *
     * @param con
     * @throws SQLException
     */
    public static ResultSet searchEventsName(Connection con, String query) throws SQLException {
        System.out.println("DB");
        System.out.println(query);
        String searchSql = "SELECT * FROM EventsData WHERE name LIKE ?;";
        PreparedStatement searchStmt = con.prepareStatement(searchSql);
        searchStmt.setString(1, '%' + query + '%');
        ResultSet results = searchStmt.executeQuery();
        return results;
    }

    /**
     * Get for events by location, partial matches are returned.
     *
     * @param con
     * @throws SQLException
     */
    public static ResultSet searchEventsLocation(Connection con, String query) throws SQLException {
        System.out.println("DB");
        System.out.println(query);
        String searchSql = "SELECT * FROM EventsData WHERE location LIKE ?;";
        PreparedStatement searchStmt = con.prepareStatement(searchSql);
        searchStmt.setString(1, '%' + query + '%');
        ResultSet results = searchStmt.executeQuery();
        return results;
    }

    /**
     * Get details for a specific event.
     *
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
            results.getDate("date"),
            results.getInt("capacity")
        );
        return eventInfo;
    }

    /**
     * Returns all events user purchased.
     *
     * @param con
     * @param email
     * @return
     * @throws SQLException
     */
    public static ResultSet eventsPurchased(Connection con, String email) throws SQLException {
        //String purchasedEventsSql = "SELECT * FROM EventsData WHERE id = (SELECT event_id FROM EventsAndGuests WHERE email = ?);";
        String purchasedEventsSql = "SELECT * FROM EventsAndGuests WHERE email = ?;";
        PreparedStatement purchasedEventsStmt = con.prepareStatement(purchasedEventsSql);
        purchasedEventsStmt.setString(1, email);
        ResultSet results = purchasedEventsStmt.executeQuery();
        return results;
    }

    /**
     * Update event's name.
     *
     * @param con
     * @param eventId
     * @param name
     * @throws SQLException
     */
    public static void executeInsertEventName(Connection con, int eventId, String name) throws SQLException {
        String insertEventNameSql = "UPDATE EventsData SET name = ? WHERE id = ?;";
        PreparedStatement insertEventNameStmt = con.prepareStatement(insertEventNameSql);
        insertEventNameStmt.setString(1, name);
        insertEventNameStmt.setInt(2, eventId);
        insertEventNameStmt.executeUpdate();
    }

    /**
     * Updates event's location.
     *
     * @param con
     * @param eventId
     * @param location
     * @throws SQLException
     */
    public static void executeInsertEventLocation(Connection con, int eventId, String location) throws SQLException {
        String insertClientLocationSql = "UPDATE EventsData SET location = ? WHERE id = ?;";
        PreparedStatement insertClientLocationStmt = con.prepareStatement(insertClientLocationSql);
        insertClientLocationStmt.setString(1, location);
        insertClientLocationStmt.setInt(2, eventId);
        insertClientLocationStmt.executeUpdate();
    }

    /**
     * Updates event's date.
     *
     * @param con
     * @param eventId
     * @param date
     * @throws SQLException
     */
    public static void executeInsertEventDate(Connection con, int eventId, Date date) throws SQLException {
        String insertEventDateSql = "UPDATE EventsData SET date = ? WHERE id = ?;";
        PreparedStatement insertEventDateStmt = con.prepareStatement(insertEventDateSql);
        insertEventDateStmt.setDate(1, date);
        insertEventDateStmt.setInt(2, eventId);
        insertEventDateStmt.executeUpdate();
    }

    public static void executeInsertEventStandardPrice(Connection con, int eventId, float price) throws SQLException {
        String insertEventPriceSql = "UPDATE EventsData SET price = ? WHERE id = ?;";
        PreparedStatement insertEventPriceStmt = con.prepareStatement(insertEventPriceSql);
        insertEventPriceStmt.setFloat(1, price);
        insertEventPriceStmt.setInt(2, eventId);
        insertEventPriceStmt.executeUpdate();
    }

    public static void executeInsertEventStudentPrice(Connection con, int eventId, float price) throws SQLException {
        String insertEventPriceSql = "UPDATE EventsData SET price_student = ? WHERE id = ?;";
        PreparedStatement insertEventPriceStmt = con.prepareStatement(insertEventPriceSql);
        insertEventPriceStmt.setFloat(1, price);
        insertEventPriceStmt.setInt(2, eventId);
        insertEventPriceStmt.executeUpdate();
    }

    public static void executeInsertEventVIPPrice(Connection con, int eventId, float price) throws SQLException {
        String insertEventPriceSql = "UPDATE EventsData SET price_VIP = ? WHERE id = ?;";
        PreparedStatement insertEventPriceStmt = con.prepareStatement(insertEventPriceSql);
        insertEventPriceStmt.setFloat(1, price);
        insertEventPriceStmt.setInt(2, eventId);
        insertEventPriceStmt.executeUpdate();
    }
}
