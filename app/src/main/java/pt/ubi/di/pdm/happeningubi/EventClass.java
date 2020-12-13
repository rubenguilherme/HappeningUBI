package pt.ubi.di.pdm.happeningubi;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class EventClass implements Serializable {

    private String name, description, location, user;
    private ArrayList<Long> images;
    private Date date;
    private long userID, eventID;
    private FirebaseFirestore db;

    public EventClass(String name, String description, String location, String user, ArrayList<Long> images, Date date, long userID, long eventID) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.user = user;
        this.images = images;
        this.date = date;
        this.userID = userID;
        this.eventID = eventID;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<Long> getImages() {
        return images;
    }

    public void setImages(ArrayList<Long> images) {
        this.images = images;
    }

    public void addImage(long id) {
        images.add(id);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

}
