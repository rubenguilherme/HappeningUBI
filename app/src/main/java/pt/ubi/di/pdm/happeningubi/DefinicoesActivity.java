package pt.ubi.di.pdm.happeningubi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class DefinicoesActivity extends AppCompatActivity {

    TextView Idioma,Mudar_Foto,Reportar_Problema,Conectar_Google,Conectar_Facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

        Idioma = (TextView) findViewById(R.id.mudar_idioma_definicoes_textview);
        Mudar_Foto = (TextView) findViewById(R.id.mudar_foto_definicoes_textview);
        Reportar_Problema = (TextView) findViewById(R.id.reportar_problema_definicoes_textview);
        Conectar_Facebook = (TextView) findViewById(R.id.conectar_facebook_definicoes_textview);
        Conectar_Google = (TextView) findViewById(R.id.conectar_google_definicoes_textview);


    }
}