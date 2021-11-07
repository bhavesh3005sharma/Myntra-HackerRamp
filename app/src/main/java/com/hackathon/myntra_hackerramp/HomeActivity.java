package com.hackathon.myntra_hackerramp;

import static java.security.AccessController.getContext;
import static java.text.DateFormat.getDateTimeInstance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackathon.myntra_hackerramp.adapter.homeAdapter;
import com.hackathon.myntra_hackerramp.model.Item;
import com.hackathon.myntra_hackerramp.model.Model;
import com.hackathon.myntra_hackerramp.model.Vote;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements  homeAdapter.listener{
    homeAdapter adapter;
    ArrayList<Model> list=new ArrayList<>();
    RecyclerView recyclerView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView=findViewById(R.id.recycler);
        mAuth=FirebaseAuth.getInstance();

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
        list.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Sketches");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name=String.valueOf(ds.child("username").getValue());
                    String uid=String.valueOf(ds.child("uid").getValue());
                    String time = null;
                    long timeStamp = 0;
                    Map<String, Long> map = new HashMap();
                    if (ds.child("timeStampmap").child("timeStamp").exists()) {
                        timeStamp = (long) ds.child("timeStampmap").child("timeStamp").getValue();
                        DateFormat dateFormat = getDateTimeInstance();
                        Date netDate = (new Date(timeStamp));
                        time = dateFormat.format(netDate);
                        map.put("timeStamp", timeStamp);
                    } else {
                        if (ds.child("timeStampStr").exists()) {
                            time = ds.child("timeStampStr").getValue().toString();
                        }
                    }

                    String sketchUrl=String.valueOf(ds.child("designUrl").getValue());
                    String mlUrl=String.valueOf(ds.child("mlUrl").getValue());
                    long upVotesNo=(long) ds.child("upvotes").getValue();
                    ArrayList<Vote> upvoters = new ArrayList<>();
                    if (ds.hasChild("listOfUpvoters"))
                        for (DataSnapshot s : ds.child("listOfUpvoters").getChildren())
                            upvoters.add(new Vote(s.child("itemId").getValue().toString(), s.child("uid").getValue().toString()));

                    int voteStatus = 0;
                    if (ds.child("listOfUpvoters").hasChild(mAuth.getUid()))
                        voteStatus = 1;

                    ArrayList<Item> similarItems=new ArrayList<>();
                    if(ds.hasChild("itemArrayList"))
                        for(DataSnapshot s:ds.child("itemArrayList").getChildren())
                            similarItems.add(new Item(s.child("itemId").getValue().toString(),s.child("picUrl").getValue().toString(),((long) s.child("price").getValue())));

                    String key=ds.getKey().toString();

                    list.add(new Model(uid,name,sketchUrl,mlUrl,upVotesNo,upvoters,time,voteStatus,similarItems,key));
                }
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                    if (list.isEmpty()) {
                        Toast.makeText(HomeActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference upVotesRef = databaseReference.child("Sketches").child(model.getKey()).child("listOfUpvoters");
        DatabaseReference complaintRef = databaseReference.child("Sketches").child(model.getKey());

        Vote upvote = new Vote(model.getKey(), mAuth.getUid());

        upVotesRef.child(mAuth.getUid()).setValue(upvote).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            complaintRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    HashMap<String, Object> data = new HashMap<>();
                                    if (task.getResult().hasChild("listOfUpvoters"))
                                        data.put("upvotes", task.getResult().child("listOfUpvoters").getChildrenCount());
                                    else data.put("upvotes", 0);
                                    complaintRef.updateChildren(data);
                                }
                            });
                        }
                    });
                }

    @Override
    public void onDesignClicked(Model model) {

    }
}