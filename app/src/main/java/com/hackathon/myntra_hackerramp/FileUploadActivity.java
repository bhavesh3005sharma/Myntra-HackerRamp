package com.hackathon.myntra_hackerramp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.agrawalsuneet.dotsloader.loaders.TashieLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hackathon.myntra_hackerramp.model.Item;
import com.hackathon.myntra_hackerramp.model.Vote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FileUploadActivity extends AppCompatActivity {

    String designUrl="", MLUrl="",prevActivity="";
    Button uploadButton;
    private FirebaseAuth mAuth;
    ArrayList<Vote> listOfUpvoters;
    int coins = 0;
    TashieLoader loader;
    TextView tv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);


        uploadButton = findViewById(R.id.btnUpload);
        loader = findViewById(R.id.tashieLoader2);
        tv = findViewById(R.id.textView);

        prevActivity = getIntent().getStringExtra("from");

        if(prevActivity.equals("home")){
            uploadButton.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
        }
        else{
            uploadButton.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
        }


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInDatabase();
            }
        });
    }

    private void saveInDatabase() {

        loader.setVisibility(View.VISIBLE);

        String uid = mAuth.getCurrentUser().getUid();
        String email = mAuth.getCurrentUser().getEmail();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        String uniqueId = uid+saveCurrentDate+saveCurrentTime;

        Map sketchData = new HashMap();
        sketchData.put("image_url",designUrl);
        sketchData.put("ml_image_url",MLUrl);
        sketchData.put("likes",listOfUpvoters);
        sketchData.put("coins",coins);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("sketches").child(email);
        db.child(uniqueId).setValue(sketchData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                            loader.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(ContextCompat.getColor(FileUploadActivity.this, R.color.red));
                        snackbar.setTextColor(ContextCompat.getColor(FileUploadActivity.this,R.color.white));
                        snackbar.show();
                    }
                });

    }
}