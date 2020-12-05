package pt.ubi.di.pdm.happeningubi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void toFeed(View view) {        //Ao clicar em qualquer sítio do ecrã, passa para a próxima atividade, ou seja, para a página do feed
        Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
        startActivity(intent);
        finish();
    }

    public void toRegisto(View view) {        //Ao clicar em qualquer sítio do ecrã, passa para a próxima atividade, ou seja, para a página para efetuar o registo
        Intent intent = new Intent(LoginActivity.this, RegistoActivity.class);
        startActivity(intent);
        finish();
    }
}