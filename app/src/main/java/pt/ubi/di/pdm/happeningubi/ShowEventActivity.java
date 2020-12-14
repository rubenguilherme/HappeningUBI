package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ShowEventActivity extends Util {

    private ImageView imagem_evento,showevent_Back;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private TextView Descricao,Titulo,Date,Horas, Local;
    private StorageReference storageRef;
    private static final String TAG = "ShowEventActivity";

    private String descricao,titulo,localizacao,horas,time_stamp_data;
    private ArrayList<Long> imagens = new ArrayList<>();

    EventClass event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        Toolbar oToolBar = (Toolbar) findViewById(R.id.showevent_bar);
        setSupportActionBar(oToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        storageRef = FirebaseStorage.getInstance().getReference();
        //get_event_extras0
        Intent getEvent = getIntent();
        event = (EventClass) getEvent.getSerializableExtra("event");
        getEventDetails(event);
        Log.d(TAG, event.toString());

        //
        Titulo = (TextView) findViewById(R.id.nome_evento_textview);
        Descricao = (TextView) findViewById(R.id.descricao_evento_textview);
        Date = (TextView) findViewById(R.id.date_evento_textview);
        Horas = (TextView) findViewById(R.id.horas_evento_textview);
        Local = (TextView) findViewById(R.id.localizaca_evento);
        imagem_evento = (ImageView) findViewById(R.id.imagem_evento_imageview);
        showevent_Back = (ImageView) findViewById(R.id.showevent_back);
        showevent_Back.setOnClickListener(v -> finish());

        Titulo.setText(titulo);
        Descricao.setText(descricao);
        Date.setText(time_stamp_data);
        Horas.setText(horas);
        Local.setText(localizacao);

        //clicar next and before to change event image
        if (event.getImages().size() > 0) {
            GlideApp.with(this).load(storageRef.child("images/" + event.getImages().get(0) + ".jpg")).into(imagem_evento);
        }
        else imagem_evento.setVisibility(View.GONE);
    }

    private void getEventDetails(EventClass event){
        titulo = event.getName();
        descricao = event.getDescription();
        localizacao = event.getLocation();
        Calendar cal = Calendar.getInstance();
        cal.setTime(event.getDate());
        String d = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR) + "  ";
        int h = cal.get(Calendar.HOUR_OF_DAY), m = cal.get(Calendar.MINUTE);
        time_stamp_data = d;
        horas = h + ":" + m;
        imagens = event.getImages();

    }
}