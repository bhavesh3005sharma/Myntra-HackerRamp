package com.hackathon.myntra_hackerramp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hackathon.myntra_hackerramp.databinding.ActivityFileUploadBinding;
import com.hackathon.myntra_hackerramp.model.Model;
import com.squareup.picasso.Picasso;

public class FileUploadActivity extends AppCompatActivity {
    ActivityFileUploadBinding binding;
    SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
    String email = sharedPreferences.getString("email", null);
    Model data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFileUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Uri uri = Uri.parse(getIntent().getStringExtra("uri"));
        Picasso.get().load(uri).into(binding.imageView);
    }

    public void postTheImage(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sketches");
//        myRef.child(email).child("")
    }
}