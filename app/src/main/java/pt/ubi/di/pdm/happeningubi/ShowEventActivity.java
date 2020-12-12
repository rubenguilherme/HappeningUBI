package pt.ubi.di.pdm.happeningubi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ShowEventActivity extends AppCompatActivity {

    private ImageView edit_descricao;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private TextView Descricao;
    private EditText NewDescricao;
    private Button Cancelar,Editar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        Toolbar oToolBar = (Toolbar) findViewById(R.id.profile_bar);
        setSupportActionBar(oToolBar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        Descricao = (TextView) findViewById(R.id.descricao_evento_textview);
        edit_descricao = (ImageView) findViewById(R.id.edit_descricao_imageview);
        edit_descricao.setOnClickListener(v -> createPopup());

    }
    private void createPopup(){
        dialogbuilder = new AlertDialog.Builder(this);
        final View EditDescPopupView = getLayoutInflater().inflate(R.layout.popup_editar_descricao_evento,null);
        NewDescricao = (EditText) EditDescPopupView.findViewById(R.id.nova_descricao_editText);
        Editar = (Button) EditDescPopupView.findViewById(R.id.editar_descricao_popup_button);
        Cancelar = (Button) EditDescPopupView.findViewById(R.id.cancelar_editar_descricao_popup_button);

        Editar.setOnClickListener(v -> {
            String nova_descricao = NewDescricao.getText().toString().trim();
            //Inserir na base de dados

            Toast.makeText(ShowEventActivity.this,"Enviado com sucesso",Toast.LENGTH_SHORT).show();
        });
        Cancelar.setOnClickListener(v -> dialog.dismiss());

    }
}