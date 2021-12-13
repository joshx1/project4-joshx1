package ConnectionPool;

import org.apache.commons.dbcp2.BasicDataSource;
import utilities.ConfigSql;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A connection pool using Apache DBCP ConnectionPool which handles our connections to the MySQL database on Stargate.
 * Taken from https://www.baeldung.com/java-connection-pooling
 * Referenced Sami's code at https://github.com/CS601-F21/code-examples.
 */
public class DBCPDataSource {

    // Apache commons connection pool implementation
    private static BasicDataSource ds = new BasicDataSource();

    // This code inside the static block is executed only once: the first time the class is loaded into memory.
    // -- https://www.geeksforgeeks.org/static-blocks-in-java/
    static {
        String configFilename = "configsql.json";

        Gson gson = new Gson();
        ConfigSql config = null;
        try {
            config = gson.fromJson(new FileReader(configFilename), ConfigSql.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // TODO: do something other than exit the whole program
        // if the config file cannot be found
        if(config == null) {
            System.exit(1);
        }
        ds.setUrl("jdbc:mysql://localhost:3306/" + config.getDatabase());
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword());
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxTotal(100);
    }

    /**
     * Return a Connection from the pool.
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DBCPDataSource(){ }
}