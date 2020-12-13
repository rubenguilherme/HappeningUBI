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

public class ShowEventActivity extends Util implements View.OnClickListener {

    private ImageView edit_descricao,imagem_evento,back_arrow,front_arrow,showevent_Back;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private TextView Descricao,Titulo,Date,Horas;
    private EditText NewDescricao;
    private Button Cancelar,Editar;
    private StorageReference storageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "ShowEventActivity";

    private static int i = 0;

    private String descricao,titulo,localizacao,horas,time_stamp_data;
    private ArrayList<Long> imagens;

    String docID;
    EventClass event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        Toolbar oToolBar = (Toolbar) findViewById(R.id.showevent_bar);
        setSupportActionBar(oToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        storageRef = FirebaseStorage.getInstance().getReference();
        //get_event_extras
        Intent getEvent = getIntent();
        event = (EventClass) getEvent.getSerializableExtra("event");
        getEventDetails(event);
        Log.d(TAG, event.toString());

        //
        Titulo = (TextView) findViewById(R.id.nome_evento_textview);
        Descricao = (TextView) findViewById(R.id.descricao_evento_textview);
        Date = (TextView) findViewById(R.id.date_evento_textview);
        Horas = (TextView) findViewById(R.id.horas_evento_textview);
        imagem_evento = (ImageView) findViewById(R.id.imagem_evento_imageview);
        back_arrow = (ImageView) findViewById(R.id.imagem_anterior_imageview);
        front_arrow = (ImageView) findViewById(R.id.imagem_aseguir_imageview);
        showevent_Back = (ImageView) findViewById(R.id.showevent_back);

        showevent_Back.setOnClickListener(v -> {
            i = 0;
            finish();
        });

        back_arrow.setOnClickListener(this);
        front_arrow.setOnClickListener(this);

        Titulo.setText(titulo);
        Descricao.setText(descricao);
        Date.setText(time_stamp_data);
        Horas.setText(horas);

        //clicar next and before to change event image
        if (event.getImages().size() > 0) {
            GlideApp.with(this).load(storageRef.child("images/" + event.getImages().get(i) + ".jpg")).into(imagem_evento);
        }
        //

    }
    @Override
    public void onClick(View v) {
        if (event.getImages().size() > 0) {
            switch (v.getId()) {
                case R.id.imagem_anterior_imageview:
                    if ((i-1) < 0){
                        Log.d(TAG, "-1 imagem :" + i);
                        i = (event.getImages().size()) - 1;}
                    else i--;
                    GlideApp.with(this).load(storageRef.child("images/" + event.getImages().get(i) + ".jpg")).into(imagem_evento);
                    Log.d(TAG, "new imagem :" + i);
                    break;
                case R.id.imagem_aseguir_imageview:
                    if ((i+1) == event.getImages().size()){
                        Log.d(TAG, "+1 imagem :" + i);
                        i = 0;}
                    else i++;
                    GlideApp.with(this).load(storageRef.child("images/" + event.getImages().get(i) + ".jpg")).into(imagem_evento);
                    Log.d(TAG, "new imagem :" + i);
                    break;
            }
        }
    }

    private void restart(){
        Intent intent = new Intent(ShowEventActivity.this,ShowEventActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
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