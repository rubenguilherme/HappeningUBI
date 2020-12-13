package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

    private EditText name;
    private TextView email;
    private String pass, docID, emailS;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = (EditText)findViewById(R.id.nameET);
        email = (TextView) findViewById(R.id.changeEmailTV);

        //read UserID from file(?)
        //example: -1

        Long userID = -1L; //read from file


        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                Long i = (Long)document.getData().get("id");
                                if(i == userID){
                                    //aux
                                    emailS = (String)document.getData().get("email");

                                    docID = document.getId();
                                    name.setText((String)document.getData().get("name"));
                                    email.setText(emailS);
                                    pass = (String)document.getData().get("password");


                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void cancelChanges(View v){ finish(); }

    public void confirmChanges(View v){

        db.collection("users").document(docID).update("name", String.valueOf(name.getText()));

        finish();
    }

    public void changePass(View v){

        FirebaseAuth.getInstance().sendPasswordResetEmail(emailS)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent." );
                        }
                    }
                });
    }

    public void deleteAcc(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

        db.collection("users").document(docID).delete();
        FirebaseAuth.getInstance().getCurrentUser().delete();

    }


}