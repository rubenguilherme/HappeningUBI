package pt.ubi.di.pdm.happeningubi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEventActivity extends Util implements TimePickerDialog.OnTimeSetListener {

    public static Long id;
    public static Long img_id;
    private static final String TAG = "AddEventActivity";

    EditText editTName, editTDesc, editTLoc;
    DatePicker datePicker;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    int hour, minute, flag = 0;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    ArrayList<Uri> files = new ArrayList<>();
    ArrayList<Long> id_files = new ArrayList<>();
    boolean existFile = false;
    Long userID;
    String language = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar oToolBar = (Toolbar) findViewById(R.id.feed_bar);
        setSupportActionBar(oToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        userID = Long.parseLong(readUser());

        editTName = findViewById(R.id.addevent_name);
        editTDesc = findViewById(R.id.addevent_desc);
        editTLoc = findViewById(R.id.addevent_loc);
        datePicker = findViewById(R.id.addevent_date);

        db.collection("NextIDS")
                .orderBy("events", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                id = (Long) document.getData().get("events");
                                break;
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Long i = (Long)document.getData().get("id");
                            if(i.equals(userID)){
                                language = String.valueOf(document.getData().get("language")).toLowerCase();
                                if(language.equals(""))
                                    language = "pt";
                                setAppLocale(language);
                            }
                        }
                    } else {
                        Log.w(TAG,"Error getting documents",task.getException());
                    }
                });

        //ivImage = findViewById(R.id.ivImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_addevent,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Add Event")
                .setMessage("Are you sure you want to post this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        eventAdded();
                    }
                })
                .setNegativeButton("No", null)
                .show();
        return super.onOptionsItemSelected(item);
    }

    public void eventAdded() {
        Map<String, Object> event = new HashMap<>();
        event.put("name", editTName.getText().toString());
        event.put("description", editTDesc.getText().toString());
        event.put("location", editTLoc.getText().toString());
        //Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), hour, minute);
        event.put("event_date", new Timestamp(new Date((datePicker.getYear() - 1900), datePicker.getMonth(), datePicker.getDayOfMonth(), hour, minute)));
        event.put("id", id);
        event.put("user_id", Long.parseLong(readUser()));
        event.put("images", id_files);
        db.collection("Event")
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
        if (existFile) {
            int size = 1;
            while (size <= files.size()) {
                StorageReference imagesRef = storageRef.child("images/" + id_files.get(size - 1) + ".jpg");
                UploadTask uploadTask = imagesRef.putFile(files.get(size - 1));
                size++;
            }
        }

        if(img_id != null)
            db.collection("NextIDS").document("wOf4zrNyF21HPlQiFPjJ").update("images", img_id);

        id++;
        db.collection("NextIDS").document("wOf4zrNyF21HPlQiFPjJ").update("events", id);
        //EventClass e = new EventClass(editTName.getText().toString(),editTDesc.getText().toString(), editTLoc.getText().toString(),"USER100" , null, new Date(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth()),-1);
        //Intent result = new Intent();
        //result.putExtra("event", e);
        //setResult(RESULT_OK, result);

        Map<String, Object> listG = new HashMap<>();
        listG.put("id", (id-1));
        listG.put("type", "going");
        db.collection("Lists")
                .add(listG)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });

        Map<String, Object> listI = new HashMap<>();
        listI.put("id", (id-1));
        listI.put("type", "interested");
        db.collection("Lists")
                .add(listI)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });

        super.finish();
    }

    public void goBack(View view) {
        setResult(RESULT_CANCELED);
        super.finish();
    }

    public void toDo(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else {
                db.collection("NextIDS")
                        .orderBy("images", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        img_id = (Long) document.getData().get("images") + flag;
                                        break;
                                    }
                                } else {
                                    Log.w("TAG", "Error getting documents.", task.getException());
                                }
                            }
                        });
                pickImageFromGallery();
            }
        }
        else {
            db.collection("NextIDS")
                    .orderBy("images", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    img_id = (Long) document.getData().get("images") + flag;
                                    break;
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int count;
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            ClipData aux = data.getClipData();
            if(aux == null){
                count = 1;
            }
            else{
                count = aux.getItemCount();
            }
            for(int i = 0; i < count; i++) {
                files.add(data.getData());
                existFile = true;
                id_files.add(img_id);
                img_id++;
                flag++;
            }
            if(language.equals("pt"))
                Toast.makeText(AddEventActivity.this,"Foto adicionada com sucesso",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(AddEventActivity.this,"Photo added with sucess",Toast.LENGTH_SHORT).show();
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hour = hourOfDay;
        this.minute = minute;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                }
                else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void setAppLocale(String localeCode){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf,dm);
    }
}