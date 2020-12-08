package pt.ubi.di.pdm.happeningubi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView Idioma,Mudar_Foto,Reportar_Problema,Conectar_Google,Conectar_Facebook,LogOut;

    private EditText Assunto_Problema,Descricao_Problema;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button Enviar,Cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

        Toolbar oToolBar = (Toolbar) findViewById(R.id.profile_bar);
        setSupportActionBar(oToolBar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        Idioma = (TextView) findViewById(R.id.mudar_idioma_definicoes_textview);
        Mudar_Foto = (TextView) findViewById(R.id.mudar_foto_definicoes_textview);
        Reportar_Problema = (TextView) findViewById(R.id.reportar_problema_definicoes_textview);
        Conectar_Facebook = (TextView) findViewById(R.id.conectar_facebook_definicoes_textview);
        Conectar_Google = (TextView) findViewById(R.id.conectar_google_definicoes_textview);
        LogOut = (TextView) findViewById(R.id.log_out_definicoes_textview);

        Reportar_Problema.setOnClickListener(this);
        LogOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.mudar_foto_definicoes_textview:

                break;
            case R.id.reportar_problema_definicoes_textview: //popup com "assunto" e "mensagem"
                createReport();
                break;
            case R.id.mudar_idioma_definicoes_textview: // mudar as strings

                break;
            case R.id.conectar_facebook_definicoes_textview:

                break;
            case R.id.conectar_google_definicoes_textview:

                break;
            case R.id.log_out_definicoes_textview:
                Intent log_out = new Intent(SettingsActivity.this,LoginActivity.class);
                startActivity(log_out);
                finish();
                break;
        }

    }
    private void createReport(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View ReportPopupView = getLayoutInflater().inflate(R.layout.popup_reportproblem,null);
        Assunto_Problema = (EditText) ReportPopupView.findViewById(R.id.assunto_report_edittext);
        Descricao_Problema = (EditText) ReportPopupView.findViewById(R.id.descricao_report_edittext);
        Enviar = (Button) ReportPopupView.findViewById(R.id.enviar_report_button);
        Cancelar = (Button) ReportPopupView.findViewById(R.id.cancelar_report_button);

        dialogBuilder.setView(ReportPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        Enviar.setOnClickListener(v -> {

            String Assunto,Descricao;
            Assunto = Assunto_Problema.getText().toString();
            Descricao = Descricao_Problema.getText().toString();

            //Saber o nome do utilizador que deu o feedback?

            //inserir base de dados


            //
            Toast.makeText(SettingsActivity.this,"Enviado com sucesso",Toast.LENGTH_SHORT).show(); //em caso de sucesso

        });
        Cancelar.setOnClickListener(v -> dialog.dismiss());
    }
}