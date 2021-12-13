package utilities;

/**
 * A class to store the properties of the JSON configuration file.
 * Referenced Sami's code at https://github.com/CS601-F21/code-examples.
 */
public class ConfigSql {

    private String database;
    private String username;
    private String password;

    /**
     * Config class constructor.
     * @param database
     * @param username
     * @param password
     */
    public ConfigSql(String database, String username, String password) {
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * Return the database property.
     * @return
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Return the username property.
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the password property.
     * @return
     */
    public String getPassword() {
        return password;
    }
}
