package utilities;
import java.sql.*;
/**
 * A class to maintain info about each client.
 */
public class ClientInfo {

    private String name;
    private String access_token;
    private String token_type;
    private String id_token;
    private String email;
    private boolean email_verified;
    private String location;
    private Date dob;

    public ClientInfo(String name, String access_token, String token_type, String id_token, String email, boolean email_verified, String location, Date dob) {
        this.name = name;
        this.access_token = access_token;
        this.token_type = token_type;
        this.id_token = id_token;
        this.email = email;
        this.email_verified = email_verified;
        this.location = location;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(boolean email_verified) {
        this.email_verified = email_verified;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
