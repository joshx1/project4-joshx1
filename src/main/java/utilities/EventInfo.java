package utilities;
import java.sql.*;

public class EventInfo {
    private int id;
    private String creator;
    private String name;
    private String location;
    private float price;
    private float priceVIP;
    private float priceStudent;
    private Date date;

    public EventInfo(int id, String creator, String name, String location, float price, float priceVIP, float priceStudent, Date date) {
        this.id = id;
        this.creator = creator;
        this.name = name;
        this.location = location;
        this.price = price;
        this.priceVIP = priceVIP;
        this.priceStudent = priceStudent;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPriceVIP() {
        return priceVIP;
    }

    public void setPriceVIP(float priceVIP) {
        this.priceVIP = priceVIP;
    }

    public float getPriceStudent() {
        return priceStudent;
    }

    public void setPriceStudent(float priceStudent) {
        this.priceStudent = priceStudent;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
