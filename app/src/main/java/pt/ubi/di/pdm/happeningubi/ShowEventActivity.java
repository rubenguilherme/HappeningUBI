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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ShowEventActivity extends AppCompatActivity {

    private ImageView edit_descricao,imagem_evento;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private TextView Descricao,Titulo,N_VAO,N_INTERESSADOS,Date,Horas;
    private EditText NewDescricao;
    private Button Cancelar,Editar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "ShowEventActivity";

    private String descricao_db,titulo,n_interessados,n_vao,horas;
    private DatePicker time_stamp_data;
    private ArrayList<Integer> imagens;

    String docID;
    EventClass event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        Toolbar oToolBar = (Toolbar) findViewById(R.id.showevent_bar);
        setSupportActionBar(oToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //get_event_extras
        Intent getEvent = getIntent();
        event = (EventClass) getEvent.getSerializableExtra("event");

        Log.d(TAG, event.toString());

        //
        Titulo = (TextView) findViewById(R.id.nome_evento_textview);
        Descricao = (TextView) findViewById(R.id.descricao_evento_textview);
        N_VAO = (TextView) findViewById(R.id.numero_vao_evento_textview);
        N_INTERESSADOS = (TextView) findViewById(R.id.numero_interessados_evento_textview);
        edit_descricao = (ImageView) findViewById(R.id.edit_descricao_imageview);
        Date = (TextView) findViewById(R.id.date_evento_textview);
        Horas = (TextView) findViewById(R.id.horas_evento_textview);
        imagem_evento = (ImageView) findViewById(R.id.imagem_evento_imageview);
        edit_descricao.setOnClickListener(v -> createPopup());

        //Titulo.setText(titulo);
        //Descricao.setText(descricao_db);
        //N_VAO.setText(n_vao);
        //N_INTERESSADOS.setText(n_interessados);
        //Date.setText(time_stamp_data)
        //Horas.setText(horas);

        //clicar next and before to change event image


        //

    }
    private void createPopup(){
        dialogbuilder = new AlertDialog.Builder(this);
        final View EditDescPopupView = getLayoutInflater().inflate(R.layout.popup_editar_descricao_evento,null);
        NewDescricao = (EditText) EditDescPopupView.findViewById(R.id.nova_descricao_editText);
        Editar = (Button) EditDescPopupView.findViewById(R.id.editar_descricao_popup_button);
        Cancelar = (Button) EditDescPopupView.findViewById(R.id.cancelar_editar_descricao_popup_button);



        Editar.setOnClickListener(v -> {
            String nova_descricao = NewDescricao.getText().toString().trim();
            db.collection("Event").document(docID).update("description",nova_descricao);
            restart();
            Toast.makeText(ShowEventActivity.this,"Enviado com sucesso",Toast.LENGTH_SHORT).show();
        });
        Cancelar.setOnClickListener(v -> dialog.dismiss());

    }
    private void restart(){
        Intent intent = new Intent(ShowEventActivity.this,ShowEventActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}