package utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles all the MySQL methods related to ticketing.
 */
public class DBUtilitiesTicketing {

    /**
     * Buys ticket from SQL database.
     *
     * @param con
     * @param email
     * @param eventId
     * @throws SQLException
     */
    public static void buyTicket(Connection con, String email, int eventId, String ticketType) throws SQLException {
        String checkSql = "SELECT amount FROM EventsAndGuests WHERE email = ? AND event_id = ? AND ticket_type = ?;";
        PreparedStatement checkStmt = con.prepareStatement(checkSql);
        checkStmt.setString(1, email);
        checkStmt.setInt(2, eventId);
        checkStmt.setString(3, ticketType);
        ResultSet resultSet = checkStmt.executeQuery();
        if (resultSet.next() == false) {
            String insertContactSql = "INSERT INTO EventsAndGuests (event_id, email, ticket_type) VALUES (?, ?, ?);";
            PreparedStatement insertContactStmt = con.prepareStatement(insertContactSql);
            insertContactStmt.setInt(1, eventId);
            insertContactStmt.setString(2, email);
            insertContactStmt.setString(3, ticketType);
            insertContactStmt.executeUpdate();
        }
        String increaseTicketCountSql = "UPDATE EventsAndGuests SET amount = amount + 1 WHERE email = ? AND ticket_type = ?;";
        PreparedStatement increaseTicketCountStmt = con.prepareStatement(increaseTicketCountSql);
        increaseTicketCountStmt.setString(1, email);
        increaseTicketCountStmt.setString(2, ticketType);
        increaseTicketCountStmt.executeUpdate();
        String increaseTicketsSoldSql = "UPDATE EventsData SET tickets_sold = tickets_sold + 1 WHERE id = ?;";
        PreparedStatement increaseTicketsSoldStmt = con.prepareStatement(increaseTicketsSoldSql);
        increaseTicketsSoldStmt.setInt(1, eventId);
        increaseTicketsSoldStmt.executeUpdate();
    }

    /**
     * Checks if the event is full.
     * @param con
     * @param eventId
     * @return
     * @throws SQLException
     */
    public static boolean checkIfEventFull(Connection con, int eventId) throws SQLException {
        String checkSql = "SELECT capacity FROM EventsData WHERE id = ?;";
        PreparedStatement checkStmt = con.prepareStatement(checkSql);
        checkStmt.setInt(1, eventId);
        ResultSet resultSet = checkStmt.executeQuery();
        resultSet.next();
        int capacity = resultSet.getInt("capacity");
        String soldSql = "SELECT tickets_sold FROM EventsData WHERE id = ?;";
        PreparedStatement soldStmt = con.prepareStatement(soldSql);
        soldStmt.setInt(1, eventId);
        ResultSet soldSet = soldStmt.executeQuery();
        soldSet.next();
        int soldTickets = soldSet.getInt("tickets_sold");
        if (soldTickets < capacity) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a user owns a ticket.
     * @param con
     * @param email
     * @param eventId
     * @param ticketType
     * @return
     * @throws SQLException
     */
    public static boolean checkIfOwnTicket(Connection con, String email, int eventId, String ticketType) throws SQLException {
        String checkSql = "SELECT amount FROM EventsAndGuests WHERE email = ? AND event_id = ? AND ticket_type = ?;";
        PreparedStatement checkStmt = con.prepareStatement(checkSql);
        checkStmt.setString(1, email);
        checkStmt.setInt(2, eventId);
        checkStmt.setString(3, ticketType);
        ResultSet resultSet = checkStmt.executeQuery();
        if (resultSet.next() == false) {
            return false;
        } else if (resultSet.getInt("amount") <= 0) {
            String deleteSql = "DELETE FROM EventsAndGuests WHERE email = ? AND event_id = ? AND ticket_type = ?;";
            PreparedStatement deleteStmt = con.prepareStatement(deleteSql);
            deleteStmt.setString(1, email);
            deleteStmt.setInt(2, eventId);
            deleteStmt.setString(3, ticketType);
            deleteStmt.executeUpdate();
            return false;
        }
        return true;
    }

    /**
     * Transfer ticket to another user.
     * @param con
     * @param emailSender
     * @param emailReceiver
     * @param eventId
     * @param ticketType
     * @throws SQLException
     */
    public static void transferTicket(Connection con, String emailSender, String emailReceiver, int eventId, String ticketType) throws SQLException {
        if (checkIfOwnTicket(con, emailSender, eventId, ticketType) == true) {
            if (checkIfOwnTicket(con, emailReceiver, eventId, ticketType) == false) {
                String insertContactSql = "INSERT INTO EventsAndGuests (event_id, email, ticket_type) VALUES (?, ?, ?);";
                //WHERE NOT EXISTS (SELECT email FROM EventsAndGuests WHERE event_id = ? AND email = ? AND ticket_type = ?);";
                PreparedStatement insertContactStmt = con.prepareStatement(insertContactSql);
                insertContactStmt.setInt(1, eventId);
                insertContactStmt.setString(2, emailReceiver);
                insertContactStmt.setString(3, ticketType);
                //insertContactStmt.setInt(4, eventId);
                //insertContactStmt.setString(5, emailReceiver);
                //insertContactStmt.setString(6, ticketType);
                insertContactStmt.executeUpdate();
            }
            String increaseTicketCountSql = "UPDATE EventsAndGuests SET amount = amount + 1 WHERE email = ? AND ticket_type = ?;";
            PreparedStatement increaseTicketCountStmt = con.prepareStatement(increaseTicketCountSql);
            increaseTicketCountStmt.setString(1, emailReceiver);
            increaseTicketCountStmt.setString(2, ticketType);
            increaseTicketCountStmt.executeUpdate();
            String decreaseTicketCountSql = "UPDATE EventsAndGuests SET amount = amount - 1 WHERE email = ? AND ticket_type = ?;";
            PreparedStatement decreaseTicketCountStmt = con.prepareStatement(decreaseTicketCountSql);
            decreaseTicketCountStmt.setString(1, emailSender);
            decreaseTicketCountStmt.setString(2, ticketType);
            decreaseTicketCountStmt.executeUpdate();
        }
        checkIfOwnTicket(con, emailSender, eventId, ticketType);
    }
}
