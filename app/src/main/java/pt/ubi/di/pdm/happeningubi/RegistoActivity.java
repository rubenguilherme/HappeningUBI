package pt.ubi.di.pdm.happeningubi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);
    }

    public void toFeed(View view) {        //Ao clicar em qualquer sítio do ecrã, passa para a próxima atividade, ou seja, para a página de login
        Intent intent = new Intent(RegistoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}