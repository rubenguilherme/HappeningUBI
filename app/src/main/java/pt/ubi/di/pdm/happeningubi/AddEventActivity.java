package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    EditText editTName, editTDesc, editTLoc;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar oToolBar = (Toolbar) findViewById(R.id.feed_bar);
        setSupportActionBar(oToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        editTName = findViewById(R.id.addevent_name);
        editTDesc = findViewById(R.id.addevent_desc);
        editTLoc = findViewById(R.id.addevent_loc);
        datePicker = findViewById(R.id.addevent_date);
        //setAppLocale("applanguage"); Gonçalo -> Mudar Idioma -> NAO APAGAR
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_addevent,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Add Event")
                .setMessage("Are you sure you want to post this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        eventAdded();
                    }
                })
                .setNegativeButton("No", null)
                .show();
        return super.onOptionsItemSelected(item);
    }

    public void eventAdded() {
        EventClass e = new EventClass(editTName.getText().toString(),editTDesc.getText().toString(), editTLoc.getText().toString(),"USER100" , null, new Date(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth()),-1);
        Intent result = new Intent();
        result.putExtra("event", e);
        setResult(RESULT_OK, result);
        super.finish();
    }

    public void goBack(View view) {
        setResult(RESULT_CANCELED);
        super.finish();
    }

    public void toDo(View view) {
        Toast.makeText(this, "Funcionalidade ainda não implementada.", Toast.LENGTH_SHORT).show();
    }

    private void setAppLocale(String localeCode){ //Gonçalo -> Mudar Idioma -> NAO APAGAR
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}