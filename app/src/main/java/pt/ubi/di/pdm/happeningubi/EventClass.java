package pt.ubi.di.pdm.happeningubi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class EventClass implements Serializable {

    private String name, description, location, user;
    private ArrayList<Long> images, going, interested;
    private Date date;
    private long userID, likes, dislikes, eventID;

    public EventClass(String name, String description, String location, String user, ArrayList<Long> images, Date date, long userID) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.user = user;
        this.images = images;
        this.date = date;
        this.userID = userID;
        going = new ArrayList<>();
        interested = new ArrayList<>();
    }

    public EventClass() {
        this(null,null,null,null, null, null, -1);
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

    public ArrayList<Long> getGoing() {
        return going;
    }

    public void setGoing(ArrayList<Long> going) {
        this.going = going;
    }

    public void addGoing(long id) {
        going.add(id);
    }

    public ArrayList<Long> getInterested() {
        return interested;
    }

    public void setInterested(ArrayList<Long> interested) {
        this.interested = interested;
    }

    public void addInterested(long id) {
        interested.add(id);
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

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public void addLike() {
        likes++;
    }

    public void subtractLike() {
        likes--;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public void addDislike() {
        dislikes++;
    }

    public void subtractDislike() {
        dislikes--;
    }
}
