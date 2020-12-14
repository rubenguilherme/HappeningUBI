package pt.ubi.di.pdm.happeningubi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SettingsActivity extends Util implements View.OnClickListener {

    private static final String TAG = "SettingsActivity";
    TextView Language,Report_Problem,LogOut;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
            //google
    private static int RC_SIGN_IN = 101;

    private ImageView Back;

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean isRefreshing = true;
    private String docID;
    CharSequence[] languages = {"Português","English"};

    //id user
    Long userID;
    //language user
    String language = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

        Toolbar oToolBar = (Toolbar) findViewById(R.id.settings_bar);
        setSupportActionBar(oToolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        userID = Long.parseLong(readUser());

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Long i = (Long)document.getData().get("id");
                            if(i.equals(userID)){
                                docID = document.getId();
                                language = String.valueOf(document.getData().get("language")).toLowerCase();
                                if(language.equals(""))
                                    language = "pt";
                            }
                        }
                    } else {
                        Log.w(TAG,"Error getting documents",task.getException());
                    }
                });


        Back = (ImageView) findViewById(R.id.settings_back);
        Language = (TextView) findViewById(R.id.mudar_idioma_definicoes_textview);
        LogOut = (TextView) findViewById(R.id.log_out_definicoes_textview);

        LogOut.setOnClickListener(this);
        Language.setOnClickListener(this);
        Back.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mudar_idioma_definicoes_textview: // mudar as strings
                isRefreshing = true;
                createPopUp();
                isRefreshing = false;
                break;
            case R.id.log_out_definicoes_textview:
                mAuth.signOut();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(SettingsActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings_back:
                finish();
                break;
        }

    }

    private void createPopUp(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
        int lang;
        if(language.equals("en")){
            alertDialogBuilder.setTitle("Select Language");
            lang = 1;
        }
        else{
            alertDialogBuilder.setTitle("Seleciona a Linguagem");
            lang = 0;
        }
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setSingleChoiceItems(languages, lang, (dialog, which) -> {
            int selectedItem = which;
            switch (selectedItem){
                case 0: setAppLocale("pt");
                        db.collection("users").document(docID).update("language","PT");
                        break;
                case 1: setAppLocale("en");
                        db.collection("users").document(docID).update("language","EN");
                        break;
            }
            Log.d(TAG, "docID : " + docID);
            restart();
            if(lang == 1)
                Toast.makeText(SettingsActivity.this,"Logout para aplicar as mudanças.",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(SettingsActivity.this,"Logout to apply all changes",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


    }
    private void setAppLocale(String localeCode){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
    private void restart(){
        Intent intent = new Intent(SettingsActivity.this,SettingsActivity.class);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    public void goToChangeProfile(View v){
        Intent intent = new Intent(this,EditProfileActivity.class);
        startActivity(intent);
    }
}