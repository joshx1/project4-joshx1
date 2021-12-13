package utilities;

import ConnectionPool.DBCPDataSource;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class DBUtilitiesClientEventsTest {

    @Test
    void executeInsertEventTest() {
        String email = "test0";
        String name = "test0";
        String location = "test0";
        Date date = Date.valueOf("2021-12-31");
        float price = 0;
        float priceStudent = 0;
        float priceVIP = 0;
        int capacity = 0;
        try {
            Connection con = DBCPDataSource.getConnection();
            DBUtilitiesEvents.executeInsertEvent(con, email, name, location, date, price, priceStudent, priceVIP, capacity);
            String selectSql = "SELECT * FROM EventsData WHERE email = ? AND name = ? AND location = ? " +
                "AND date = ? AND price = ? AND price_VIP = ? AND price_student AND capacity = ?;";
            PreparedStatement selectClientInfoStmt = con.prepareStatement(selectSql);
            selectClientInfoStmt.setString(1, email);
            selectClientInfoStmt.setString(2, name);
            selectClientInfoStmt.setDate(3, date);
            selectClientInfoStmt.setString(4, location);
            selectClientInfoStmt.setDouble(5, price);
            selectClientInfoStmt.setDouble(6, priceStudent);
            selectClientInfoStmt.setDouble(7, priceVIP);
            selectClientInfoStmt.setDouble(8, capacity);
            ResultSet resultSet = selectClientInfoStmt.executeQuery();
            assert(resultSet.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}