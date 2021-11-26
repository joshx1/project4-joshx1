package ConnectionPool;

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

    public static void executeInsertClientLocation(Connection con, String email, String location) throws SQLException {
        String inserClientLocationSql = "UPDATE ClientData SET location = ? WHERE email = ?;";
        PreparedStatement insertClientLocationStmt = con.prepareStatement(inserClientLocationSql);
        insertClientLocationStmt.setString(1, location);
        insertClientLocationStmt.setString(2, email);
        insertClientLocationStmt.executeUpdate();
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
        //ArrayList<HashMap> eventDetails = new ArrayList<HashMap>();
        //while(results.next()) {
        //    HashMap<String, String> nameMap = new HashMap<String, String>();
        //    nameMap.put("name", results.getString("name"));
        //    eventDetails.add(nameMap);

        //    System.out.printf("Name: %s\n", results.getString("name"));
        //    System.out.printf("Extension: %s\n", results.getInt("extension"));
        //    System.out.printf("Email: %s\n", results.getString("email"));
        //    System.out.printf("Start Date: %s\n", results.getString("startdate"));
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
