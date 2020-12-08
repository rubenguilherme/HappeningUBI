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

import java.util.ArrayList;
import java.util.Date;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<EventClass> events;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar oToolBar = (Toolbar) findViewById(R.id.feed_bar);
        setSupportActionBar(oToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        events = new ArrayList<>();

        //PARA TESTAR
        events.add(new EventClass("EVENTO1","DESCRICAO","COVILHA", "USER1", null, new Date(System.currentTimeMillis()), 1));
        events.add(new EventClass("EVENTO2","DESCRICAO","COVILHA", "USER1", null, new Date(System.currentTimeMillis()), 1));
        events.add(new EventClass("EVENTO3","DESCRICAO","COVILHA", "USER1", null, new Date(System.currentTimeMillis()), 1));
        events.add(new EventClass("EVENTO3","DESCRICAO","COVILHA", "USER1", null, new Date(System.currentTimeMillis()), 1));
        events.add(new EventClass("EVENTO3","DESCRICAO","COVILHA", "USER1", null, new Date(System.currentTimeMillis()), 1));
        events.get(0).addGoing(50);
        events.get(1).addGoing(60);
        events.get(1).addGoing(60);
        events.get(2).addGoing(70);
        events.get(2).addGoing(70);
        events.get(2).addGoing(70);
        events.get(3).addGoing(80);
        events.get(3).addGoing(80);
        events.get(3).addGoing(80);
        events.get(3).addGoing(80);
        events.get(4).addGoing(90);
        events.get(4).addGoing(90);
        events.get(4).addGoing(90);
        events.get(4).addGoing(90);
        events.get(4).addGoing(90);

        recyclerView = findViewById(R.id.feed_recyclerView);
        adapter = new EventAdapter(this,events, EventAdapter.TYPE_FEED);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                intent.putExtra("events",events);
                startActivity(intent);
                break;
            case R.id.feed_add:
                Intent intent2 = new Intent(this, AddEventActivity.class);
                startActivityForResult(intent2,10);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent iResult) {
        super.onActivityResult(requestCode, resultCode, iResult);
        if ((10 == requestCode) && (resultCode == RESULT_OK)) {
            EventClass e = (EventClass) iResult.getSerializableExtra("event");
            events.add(e);
            adapter.notifyDataSetChanged();
        }
    }
}