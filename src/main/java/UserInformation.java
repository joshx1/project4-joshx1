import java.util.ArrayList;

/**
 * A class for UserInformation data structure.
 */
public class UserInformation {

    private String name;
    private String location;
    private String age;
    private ArrayList<String> events;

    public UserInformation(String name, String location, String age, ArrayList<String> events) {
        this.name = name;
        this.location = location;
        this.age = age;
        this.events = events;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }
}
