package com.hackathon.myntra_hackerramp;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.hackathon.myntra_hackerramp.adapter.homeAdapter;
import com.hackathon.myntra_hackerramp.model.Model;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements  homeAdapter.listener{
    homeAdapter adapter;
    ArrayList<Model> list=new ArrayList<>();
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView=findViewById(R.id.recycler);

        adapter = new homeAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadData();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        adapter.setUpOnListener(this);
    }

    public void loadData(){

    }

    public void goToDrawingSheet(View V) {
        startActivity(new Intent(this, DrawingSheet.class));
    }

    public void goToTop10(View view){
        startActivity(new Intent(this, top10.class));
    }

    public void logout(View V) {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", null);
        editor.apply();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    public void upVoteClicked(Model model) {

    }

    @Override
    public void onDesignClicked(Model model) {

    }
}