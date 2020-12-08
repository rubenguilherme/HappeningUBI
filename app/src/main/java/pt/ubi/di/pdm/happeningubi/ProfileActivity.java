package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<EventClass> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar oToolBar = (Toolbar) findViewById(R.id.profile_bar);
        setSupportActionBar(oToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent iEvents = getIntent();
        events = (ArrayList<EventClass>) iEvents.getSerializableExtra("events");

        recyclerView = findViewById(R.id.profile_recycler_view);
        EventAdapter adapter = new EventAdapter(this, events, EventAdapter.TYPE_PROFILE);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    public void goBack(View view) {
        setResult(RESULT_CANCELED);
        super.finish();
    }
}