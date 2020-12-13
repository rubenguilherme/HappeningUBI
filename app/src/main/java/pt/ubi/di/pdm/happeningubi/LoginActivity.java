package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends Util {

    SignInButton btnGoogle;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static int RC_SIGN_IN = 101;
    private EditText email, password;
    private static final String TAG = "LoginActivity";
    private Long nextID;
    private String emailS;
    private boolean auxF = false;

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
            setCurrentUser(currentUser.getEmail(), currentUser, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnGoogle = findViewById(R.id.sign_in_button);
        email = findViewById(R.id.email_text);
        password = findViewById(R.id.pass_input);

        btnGoogle.setOnClickListener(v -> signIn());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                        addUserToDatabase(user.getEmail(), user.getDisplayName(), true, user);
                        Toast.makeText(LoginActivity.this, user.getEmail()+ "\n" +user.getDisplayName(), Toast.LENGTH_SHORT).show();

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).toString() , Toast.LENGTH_SHORT).show();
                        addUserToDatabase("", "", false, null);
                    }

                    // ...
                });
    }

    private void addUserToDatabase(String email, String name, boolean flag, FirebaseUser userID) {

        if(userID != null) {

            String username = email.split("@")[0];

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    auxF = true;
                                }
                                    if(!auxF) {
                                        db.collection("NextIDS")
                                                .orderBy("users", Query.Direction.DESCENDING)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        System.out.println("f3");
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "User created.", Toast.LENGTH_LONG).show();
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                nextID = (Long) document.getData().get("users");

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
                                                                db.collection("NextIDS").document("wOf4zrNyF21HPlQiFPjJ").update("users", Long.valueOf(nextID + 1));

                                                                updateUI(userID, flag);

                                                            }
                                                        } else {
                                                            Log.w(TAG, "Error getting documents.", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                    else{
                                        setCurrentUser(userID.getEmail(), userID, false);
                                    }
                                }
                            }
                    });
        }
    }

    private void updateUI(FirebaseUser user, boolean flag) {
        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){
                if(user != null) {
                    Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    if(flag){
                        email.setError("Check if email is correct");
                        password.setError("Check if password is correct");
                    }
                }
            }
        }, 100);

    }


    public void toFeed(View view) {
        emailS = email.getText().toString();
        String passwordS = password.getText().toString();

        if(emailS.equals("admin"))
            emailS = "hubiadmin@gmail.com";

        if((emailS != null && passwordS != null) && (!emailS.equals("") && !passwordS.equals(""))){
            mAuth.signInWithEmailAndPassword(emailS.trim(), passwordS.trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                setCurrentUser(emailS, user, true);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                setCurrentUser("",null, true);
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void setCurrentUser(String email, FirebaseUser user, boolean flag) {

        if(user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String aux = (String) document.getData().get("email");
                                    if (aux.equals(email)) {
                                        writeUser(String.valueOf((Long) document.getData().get("id")));
                                        updateUI(user, flag);
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }


    }

    public void toRegisto(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}