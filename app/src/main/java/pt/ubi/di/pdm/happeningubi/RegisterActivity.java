package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    EditText mNome, mEmail, mPassword, mPassConfirm;
    FirebaseAuth fAuth;
    Button mRegisterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);
        //setAppLocale("applanguage"); Gonçalo -> Mudar Idioma -> NAO APAGAR

        mNome = findViewById(R.id.username);
        mEmail = findViewById(R.id.emailRegisto);
        mPassword = findViewById(R.id.passRegisto);
        mPassConfirm = findViewById(R.id.confirm_passRegisto);
        mRegisterButton = findViewById(R.id.login_button);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent (getApplicationContext(), FeedActivity.class));
            finish();
        }

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmpassword = mPassConfirm.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");

                }else{
                    if(TextUtils.isEmpty(password)){
                        mPassword.setError("Password is required");

                    }else{
                        if(TextUtils.isEmpty(confirmpassword)){
                            mPassword.setError("Confirm your Password");

                        }else{
                            if(password.length() < 6){
                                mPassword.setError("Password must be >= 6 Characteres");

                            }else{
                                if(!confirmpassword.equals(password)){
                                    mPassConfirm.setError("Passwords must be equals.");

                                }else{
                                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "User created.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent (getApplicationContext(), FeedActivity.class));
                                            }else{
                                                Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }

                    }

                }
            }
        });

    }

    public void toFeed(View view) {        //Ao clicar em qualquer sítio do ecrã, passa para a próxima atividade, ou seja, para a página de login
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void setAppLocale(String localeCode){ //Gonçalo -> Mudar Idioma -> NAO APAGAR
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}