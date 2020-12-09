package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView Language,Change_Foto,Report_Problem,Connect_Google,Connect_Facebook,LogOut;

    private EditText Subject_Problem,Description_Problem;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button Send,Cancel;

    CharSequence[] languages = {"Português","English"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

        Toolbar oToolBar = (Toolbar) findViewById(R.id.profile_bar);
        setSupportActionBar(oToolBar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        Language = (TextView) findViewById(R.id.mudar_idioma_definicoes_textview);
        Change_Foto = (TextView) findViewById(R.id.mudar_foto_definicoes_textview);
        Report_Problem = (TextView) findViewById(R.id.reportar_problema_definicoes_textview);
        Connect_Facebook = (TextView) findViewById(R.id.conectar_facebook_definicoes_textview);
        Connect_Google = (TextView) findViewById(R.id.conectar_google_definicoes_textview);
        LogOut = (TextView) findViewById(R.id.log_out_definicoes_textview);

        Report_Problem.setOnClickListener(this);
        LogOut.setOnClickListener(this);
        Language.setOnClickListener(this);

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
                createPopUp();
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
            //clicar no user

            //break;

            //clicar no add event


            //break;
        }

    }
    private void createReport(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View ReportPopupView = getLayoutInflater().inflate(R.layout.popup_reportproblem,null);
        Subject_Problem = (EditText) ReportPopupView.findViewById(R.id.assunto_report_edittext);
        Description_Problem = (EditText) ReportPopupView.findViewById(R.id.descricao_report_edittext);
        Send = (Button) ReportPopupView.findViewById(R.id.enviar_report_button);
        Cancel = (Button) ReportPopupView.findViewById(R.id.cancelar_report_button);

        dialogBuilder.setView(ReportPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        Send.setOnClickListener(v -> {

            String Subject,Description;
            Subject = Subject_Problem.getText().toString();
            Description = Description_Problem.getText().toString();

            //Saber o nome do utilizador que deu o feedback?

            //inserir base de dados


            //
            Toast.makeText(SettingsActivity.this,"Enviado com sucesso",Toast.LENGTH_SHORT).show(); //em caso de sucesso

        });
        Cancel.setOnClickListener(v -> dialog.dismiss());
    }
    private void createPopUp(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
        //if(Ingles == 1)
            alertDialogBuilder.setTitle("Select Language");
        //else
        //  alertDialogBuilder.setTitle("Seleciona a Linguagem");
        alertDialogBuilder.setCancelable(true);
        // 0 = first element -> default _ mudar para a language do momento
        alertDialogBuilder.setSingleChoiceItems(languages, 0, (dialog, which) -> {
            int selectedItem = which;
            //change language block

            //
            Toast.makeText(SettingsActivity.this,languages[selectedItem].toString(),Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


    }
}