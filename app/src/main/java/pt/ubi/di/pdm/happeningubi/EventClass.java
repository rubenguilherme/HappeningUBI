package pt.ubi.di.pdm.happeningubi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class EventClass implements Serializable {

    private String name, description, location, user;
    private ArrayList<Integer> images, going, interested;
    private Date date;
    private int userID, likes, dislikes, eventID;

    public EventClass(String name, String description, String location, String user, ArrayList<Integer> images, Date date, int userID) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.user = user;
        this.images = images;
        this.date = date;
        this.userID = userID;
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

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void setImages(ArrayList<Integer> images) {
        this.images = images;
    }

    public void addImage(int id) {
        images.add(id);
    }

    public ArrayList<Integer> getGoing() {
        return going;
    }

    public void setGoing(ArrayList<Integer> going) {
        this.going = going;
    }

    public void addGoing(int id) {
        going.add(id);
    }

    public ArrayList<Integer> getInterested() {
        return interested;
    }

    public void setInterested(ArrayList<Integer> interested) {
        this.interested = interested;
    }

    public void addInterested(int id) {
        interested.add(id);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void addLike() {
        likes++;
    }

    public void subtractLike() {
        likes--;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public void addDislike() {
        dislikes++;
    }

    public void subtractDislike() {
        dislikes--;
    }
}
