package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import java.util.Objects;

public class FeedActivity extends Util {

    private RecyclerView recyclerView;
    private ArrayList<EventClass> events;
    private EventAdapter adapter;
    private FirebaseFirestore db;
    private static boolean restarted = true;
    String language_user = "";
    Long userID = -1l;
    String docID;
    private static final String TAG = "FeedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar oToolBar = (Toolbar) findViewById(R.id.feed_bar);
        setSupportActionBar(oToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        db = FirebaseFirestore.getInstance();
        userID = Long.parseLong(readUser());
        //setAppLocale("applanguage"); Gonçalo -> Mudar Idioma -> NAO APAGAR

        /* Quando user der login , saber a language a partir da database e mudar o idioma para todas as activities
         */
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Long i = (Long)document.getData().get("id");
                            if(i.equals(userID)){
                                docID = document.getId();
                                language_user = String.valueOf(document.getData().get("language")).toLowerCase();
                                if(language_user.equals("en"))
                                    setAppLocale("en");
                                else
                                    setAppLocale("pt");
                            }
                        }
                    } else {
                        Log.w(TAG,"Error getting documents",task.getException());
                    }
                });
        restart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_feed,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feed_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.feed_add:
                Intent intent2 = new Intent(this, AddEventActivity.class);
                startActivityForResult(intent2,10);
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
                                    EventClass e = new EventClass((String) m.get("name"),
                                            (String) m.get("description"),(String) m.get("location"),
                                            "USER", (ArrayList<Long>) m.get("images"),
                                            t,
                                            (long) m.get("user_id"), (long) m.get("id"));
                                    events.add(e);
                                }
                            }
                            recyclerView = findViewById(R.id.feed_recyclerView);
                            adapter = new EventAdapter(getApplicationContext(),events, EventAdapter.TYPE_FEED, userID);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void setAppLocale(String localeCode){ //Gonçalo -> Mudar Idioma -> NAO APAGAR
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
    private void restart(){
        if(restarted) {
            restarted = false;
            Intent intent = new Intent(FeedActivity.this, FeedActivity.class);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}