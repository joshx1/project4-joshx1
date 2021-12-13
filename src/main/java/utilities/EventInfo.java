package utilities;
import java.sql.*;

/**
 * Class for event info object.
 */
public class EventInfo {
    private int id;
    private String creator;
    private String name;
    private String location;
    private float price;
    private float priceVIP;
    private float priceStudent;
    private Date date;
    private int capacity;
    private int ticketsSold;

    /**
     * Constructor for event info.
     * @param id
     * @param creator
     * @param name
     * @param location
     * @param price
     * @param priceVIP
     * @param priceStudent
     * @param date
     * @param capacity
     * @param ticketsSold
     */
    public EventInfo(int id, String creator, String name, String location, float price, float priceVIP, float priceStudent, Date date, int capacity, int ticketsSold) {
        this.id = id;
        this.creator = creator;
        this.name = name;
        this.location = location;
        this.price = price;
        this.priceVIP = priceVIP;
        this.priceStudent = priceStudent;
        this.date = date;
        this.capacity = capacity;
        this.ticketsSold = ticketsSold;
    }

    /**
     * A getter and setter for each variable in event info.
     * @return
     */
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(int ticketsSold) {
        this.ticketsSold = ticketsSold;
    }
}
