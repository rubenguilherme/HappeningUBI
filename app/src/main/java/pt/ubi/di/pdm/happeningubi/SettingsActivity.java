package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SettingsActivity";
    TextView Language,Change_Foto,Report_Problem,Connect_Google,Connect_Facebook,LogOut;

    private EditText Subject_Problem,Description_Problem;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button Send,Cancel;
            //google
    SignInButton btnGoogle;
    private static int RC_SIGN_IN = 101;

    GoogleSignInClient mGoogleSignInClient;
    private ImageView Back;

    private FirebaseAuth mAuth;

    private boolean isRefreshing = true;

    CharSequence[] languages = {"Português","English"};

    //id user
    Long userID = 0l;
    //language user
    String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

        Toolbar oToolBar = (Toolbar) findViewById(R.id.settings_bar);
        setSupportActionBar(oToolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Long i = (Long)document.getData().get("id");
                            if(i.equals(userID)){
                                language = (String) document.getData().get("language");
                                language = Objects.requireNonNull(language).toLowerCase();
                            }
                        }
                    } else {
                        Log.w(TAG,"Error getting documents",task.getException());
                    }
                });


        btnGoogle = findViewById(R.id.sign_in_button_settings);
        Back = (ImageView) findViewById(R.id.settings_back);
        Language = (TextView) findViewById(R.id.mudar_idioma_definicoes_textview);
        Report_Problem = (TextView) findViewById(R.id.reportar_problema_definicoes_textview);
        LogOut = (TextView) findViewById(R.id.log_out_definicoes_textview);

        Report_Problem.setOnClickListener(this);
        LogOut.setOnClickListener(this);
        Language.setOnClickListener(this);
        Back.setOnClickListener(this);
        btnGoogle.setOnClickListener(v -> signIn());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(SettingsActivity.this, gso);

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.reportar_problema_definicoes_textview: //popup com "assunto" e "mensagem"
                createReport();
                break;
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        Toast.makeText(SettingsActivity.this, user.getEmail()+"\n" + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SettingsActivity.this, Objects.requireNonNull(task.getException()).toString() , Toast.LENGTH_SHORT).show();
                    }

                    // ...
                });
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
            //change language block
            switch (selectedItem){
                case 0: setAppLocale("pt"); //set user language on data base
                        break;
                case 1: setAppLocale("en"); //set user language on data base
                        break;
            }
            restart();
            Toast.makeText(SettingsActivity.this,languages[selectedItem].toString(),Toast.LENGTH_SHORT).show();
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