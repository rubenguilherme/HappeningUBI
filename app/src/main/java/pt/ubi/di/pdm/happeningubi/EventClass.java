package pt.ubi.di.pdm.happeningubi;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class EventClass implements Serializable {

    private String name, description, location, user;
    private ArrayList<Long> images, going, interested;
    private Timestamp date;
    private long userID, eventID;
    private FirebaseFirestore db;

    public EventClass(String name, String description, String location, String user, ArrayList<Long> images, Timestamp date, long userID, long eventID) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.user = user;
        this.images = images;
        this.date = date;
        this.userID = userID;
        this.eventID = eventID;
        going = new ArrayList<>();
        interested = new ArrayList<>();
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
        db = FirebaseFirestore.getInstance();
        db.collection("Lists")
                .whereEqualTo("type", "interested")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map m = document.getData();
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        interested.add(id);
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

}
