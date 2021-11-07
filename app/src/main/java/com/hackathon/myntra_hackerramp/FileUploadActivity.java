package com.hackathon.myntra_hackerramp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hackathon.myntra_hackerramp.databinding.ActivityFileUploadBinding;
import com.hackathon.myntra_hackerramp.model.Item;
import com.hackathon.myntra_hackerramp.model.Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FileUploadActivity extends AppCompatActivity {
    ActivityFileUploadBinding binding;
    Model data;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("sketches");
    String imageId = db.push().getKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFileUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String prevActivity = getIntent().getStringExtra("from");
        data = (Model) getIntent().getSerializableExtra("data");

        if (prevActivity.equals("home")) {
            binding.btnUpload.setVisibility(View.GONE);
            binding.textView.setVisibility(View.GONE);
        } else if (prevActivity.equals("drawing_sheet")) {
            binding.btnUpload.setVisibility(View.VISIBLE);
            binding.textView.setVisibility(View.VISIBLE);
        }


        if (data.getDesignUrl() != null)
            Picasso.get().load(Uri.parse(data.getDesignUrl())).into(binding.design);
        if (data.getMlUrl() != null)
            Picasso.get().load(Uri.parse(data.getMlUrl())).into(binding.mlitem);
        int sz = (data.getItemArrayList() != null) ? data.getItemArrayList().size() : 0;
        if (sz > 0 && data.getItemArrayList().get(0).getPicUrl() != null) {
            Glide.with(this).load(data.getItemArrayList().get(0).getPicUrl()).into(binding.item1);
            binding.price1.setText("Rs. " + data.getItemArrayList().get(0).getPrice());
        }
        if (sz > 1 && data.getItemArrayList().get(1).getPicUrl() != null) {
            Glide.with(this).load(data.getItemArrayList().get(1).getPicUrl()).into(binding.item2);
            binding.price2.setText("Rs. " + data.getItemArrayList().get(1).getPrice());
        }
        if (sz > 2 && data.getItemArrayList().get(2).getPicUrl() != null) {
            Glide.with(this).load(data.getItemArrayList().get(2).getPicUrl()).into(binding.item3);
            binding.price3.setText("Rs. " + data.getItemArrayList().get(2).getPrice());
        }

        binding.btnUpload.setOnClickListener(v -> upload_picture_in_cloud());
    }

    private void saveInDatabase() {
        binding.tashieLoader2.setVisibility(View.VISIBLE);
        Map timeStampmap = new HashMap();
        timeStampmap.put("timeStamp", ServerValue.TIMESTAMP);
        data.setTimeStampmap(timeStampmap);
        db.child(imageId).setValue(data)
                .addOnSuccessListener(unused -> {
                    binding.tashieLoader2.setVisibility(View.GONE);
                    Toast.makeText(this, "Post Uploaded", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    binding.tashieLoader2.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(ContextCompat.getColor(FileUploadActivity.this, R.color.red));
                    snackbar.setTextColor(ContextCompat.getColor(FileUploadActivity.this, R.color.white));
                    snackbar.show();
                });
    }

    public void upload_picture_in_cloud() {
        binding.tashieLoader2.setVisibility(View.VISIBLE);
        StorageReference srefer = FirebaseStorage.getInstance().getReference().child("images")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(imageId);
        srefer.putFile(Uri.parse(data.getDesignUrl())).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                srefer.getDownloadUrl().addOnSuccessListener(uri -> {
                    data.setDesignUrl(uri.toString());
                    data.setMlUrl(uri.toString());
                    saveInDatabase();
                }).addOnFailureListener(e -> {
                    binding.tashieLoader2.setVisibility(View.GONE);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                binding.tashieLoader2.setVisibility(View.GONE);
                Toast.makeText(this, "Picture failed to be Uploaded", Toast.LENGTH_SHORT).show();
            }

        });
    }

}