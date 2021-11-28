public class EventInformation {

    private String name;
    private String date;
    private String time;
    private String price;
    private String location;
    private String ageLimit;

    public EventInformation(String name, String date, String time, String price, String location, String ageLimit) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.price = price;
        this.location = location;
        this.ageLimit = ageLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }
}
