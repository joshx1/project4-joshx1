package utilities;

import ConnectionPool.DBCPDataSource;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class DBUtilitiesClientTest {

    @Test
    void executeInsertSessionData() {
        String email = "test0";
        String sessionId = "test0";
        try {
            Connection con = DBCPDataSource.getConnection();
            DBUtilitiesClient.executeInsertSessionData(con, sessionId, email);
            String selectSql = "SELECT * FROM SessionData WHERE email = ? AND sessionId = ? ;";
            PreparedStatement selectClientInfoStmt = con.prepareStatement(selectSql);
            selectClientInfoStmt.setString(1, email);
            selectClientInfoStmt.setString(2, sessionId);
            ResultSet resultSet = selectClientInfoStmt.executeQuery();
            assert(resultSet.next());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void executeInsertClientDOB() {
        String email = "test0";
        Date dob = Date.valueOf("2000-01-01");
        try {
            Connection con = DBCPDataSource.getConnection();
            DBUtilitiesClient.executeInsertClientDOB(con, email, dob);
            String selectSql = "SELECT * FROM ClientData WHERE email = ? AND dob = ? ;";
            PreparedStatement selectClientInfoStmt = con.prepareStatement(selectSql);
            selectClientInfoStmt.setString(1, email);
            selectClientInfoStmt.setDate(2, dob);
            ResultSet resultSet = selectClientInfoStmt.executeQuery();
            assert(resultSet.next());
    } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}