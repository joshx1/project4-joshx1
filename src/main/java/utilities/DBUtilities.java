package utilities;

import java.sql.*;

/**
 * This class handles all the MySQL methods related to client information and creation.
 */
public class DBUtilities {

    /**
     * A method to insert session data and a user's email.
     */
    public static void executeInsertSessionData(Connection con, String sessionId, String email) throws SQLException {
        String insertContactSql = "INSERT INTO SessionData (sessionId, email) VALUES (?, ?);";
        PreparedStatement insertContactStmt = con.prepareStatement(insertContactSql);
        insertContactStmt.setString(1, sessionId);
        insertContactStmt.setString(2, email);
        insertContactStmt.executeUpdate();
    }

    /**
     * Creates and inserts a new client.
     * @param con
     * @param name
     * @param access_token
     * @param token_type
     * @param id_token
     * @param email
     * @param email_verified
     * @throws SQLException
     */
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
     *
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
     * Updates client's DOB.
     *
     * @param con
     * @param email
     * @param dob
     * @throws SQLException
     */
    public static void executeInsertClientDOB(Connection con, String email, Date dob) throws SQLException {
        String insertClientDOBSql = "UPDATE ClientData SET dob = ? WHERE email = ?;";
        PreparedStatement insertClientDOBStmt = con.prepareStatement(insertClientDOBSql);
        System.out.println(dob);
        System.out.println(email);
        insertClientDOBStmt.setDate(1, dob);
        insertClientDOBStmt.setString(2, email);
        insertClientDOBStmt.executeUpdate();
    }

    /**
     * Changes a user's name.
     * @param con
     * @param email
     * @param name
     * @throws SQLException
     */
    public static void executeInsertClientName(Connection con, String email, String name) throws SQLException {
        String insertClientNameSql = "UPDATE ClientData SET name = ? WHERE email = ?;";
        PreparedStatement insertClientNameStmt = con.prepareStatement(insertClientNameSql);
        insertClientNameStmt.setString(1, name);
        insertClientNameStmt.setString(2, email);
        insertClientNameStmt.executeUpdate();
    }

    /**
     * Checks if a client exists.
     * @param con
     * @param email
     * @return
     * @throws SQLException
     */
    public static int checkClientExists(Connection con, String email) throws SQLException {
        String checkClientExist = "SELECT COUNT(1) FROM ClientData WHERE email = ?;";
        PreparedStatement checkClientStmt = con.prepareStatement(checkClientExist);
        checkClientStmt.setString(1, email);
        ResultSet results = checkClientStmt.executeQuery();
        results.next();
        return results.getInt(1);
    }

    /**
     * Returns email for a given session ID.
     *
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
     *
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
            results.getString("location"),
            results.getDate("dob"));
        return clientInfo;
    }
}
