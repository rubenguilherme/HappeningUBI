package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ProfileActivity extends Util {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ArrayList<EventClass> events;
    private Long userID;
    private EventAdapter adapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar oToolBar = (Toolbar) findViewById(R.id.profile_bar);
        setSupportActionBar(oToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        db = FirebaseFirestore.getInstance();
        userID = Long.parseLong(readUser());
        TextView tvUsername = findViewById(R.id.profile_username);
        db.collection("users")
                .whereEqualTo("id", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map m = document.getData();
                                username = m.get("username").toString();
                                tvUsername.setText("@" + username);
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadEvents() {
        events = new ArrayList<>();
        db.collection("Event")
                .orderBy("event_date", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map m = document.getData();
                                Timestamp t = ((Timestamp) m.get("event_date"));
                                if ((t.getSeconds() + 86400) > Timestamp.now().getSeconds()) {
                                    Long uid = (Long) m.get("user_id");
                                    if (uid == Long.parseLong(readUser())) {
                                        EventClass e = new EventClass((String) m.get("name"),
                                                (String) m.get("description"), (String) m.get("location"),
                                                username, (ArrayList<Long>) m.get("images"),
                                                t.toDate(),
                                                (long) m.get("user_id"), (long) m.get("id"));
                                        events.add(e);
                                    }
                                }
                            }
                            adapter = new EventAdapter(getApplicationContext(), events, EventAdapter.TYPE_PROFILE, userID);
                            recyclerView = findViewById(R.id.profile_recycler_view);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void goBack(View view) {
        setResult(RESULT_CANCELED);
        super.finish();
    }
}