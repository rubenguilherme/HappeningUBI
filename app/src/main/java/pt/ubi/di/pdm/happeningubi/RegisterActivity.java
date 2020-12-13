package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends Util {

    EditText mNome, mUser, mEmail, mPassword, mPassConfirm;
    FirebaseAuth fAuth;
    Button mRegisterButton;
    Long nextID;

    private static final String TAG = "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);
        //setAppLocale("applanguage"); Gonçalo -> Mudar Idioma -> NAO APAGAR

        mNome = findViewById(R.id.name);
        mUser = findViewById(R.id.username);
        mEmail = findViewById(R.id.emailRegisto);
        mPassword = findViewById(R.id.passRegisto);
        mPassConfirm = findViewById(R.id.confirm_passRegisto);
        mRegisterButton = findViewById(R.id.login_button);

        fAuth = FirebaseAuth.getInstance();

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNome.getText().toString().trim();
                String username = mUser.getText().toString().trim();
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

                                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                                db.collection("NextIDS")
                                                        .orderBy("users", Query.Direction.DESCENDING)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(RegisterActivity.this, "User created.", Toast.LENGTH_LONG).show();
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        nextID = (Long)document.getData().get("users");

                                                                        Map<String, Object> user = new HashMap<>();
                                                                        user.put("email", email);
                                                                        user.put("id", nextID);
                                                                        user.put("language", "");
                                                                        user.put("name", name);
                                                                        user.put("profile_pic_id", -1L);
                                                                        user.put("username", username);

                                                                        writeUser(String.valueOf(nextID));

                                                                        // Add a new document with a generated ID
                                                                        db.collection("users")
                                                                                .add(user)
                                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Log.d(TAG, "Error adding document", e);
                                                                                    }
                                                                                });

                                                                        //update table ID
                                                                        db.collection("NextIDS").document("wOf4zrNyF21HPlQiFPjJ").update("users", Long.valueOf(nextID+1));

                                                                        startActivity(new Intent (getApplicationContext(), FeedActivity.class));
                                                                    }
                                                                } else {
                                                                    Log.w("TAG", "Error getting documents.", task.getException());
                                                                }
                                                            }
                                                        });
                                            }
                                            else{
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
        //Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        //startActivity(intent);
        //finish();
    }

    private void setAppLocale(String localeCode){ //Gonçalo -> Mudar Idioma -> NAO APAGAR
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}