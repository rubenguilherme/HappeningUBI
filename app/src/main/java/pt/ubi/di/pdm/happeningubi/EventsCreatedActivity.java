package pt.ubi.di.pdm.happeningubi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class EventsCreatedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<EventClass> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_created);

        Toolbar oToolBar = (Toolbar) findViewById(R.id.created_bar);
        setSupportActionBar(oToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent iEvents = getIntent();
        events = (ArrayList<EventClass>) iEvents.getSerializableExtra("events");

        recyclerView = findViewById(R.id.created_recycler_view);
        EventAdapter adapter = new EventAdapter(this, events, EventAdapter.TYPE_CREATED);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void goBack(View view) {
        setResult(RESULT_CANCELED);
        super.finish();
    }
}