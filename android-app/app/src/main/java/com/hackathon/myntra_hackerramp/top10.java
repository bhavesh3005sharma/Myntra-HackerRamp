package com.hackathon.myntra_hackerramp;

import static java.text.DateFormat.getDateTimeInstance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

public class top10 extends AppCompatActivity implements homeAdapter.listener{
    homeAdapter adapter;
    ArrayList<Model> list=new ArrayList<>();
    RecyclerView recyclerView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);
        recyclerView=findViewById(R.id.recycler);
        mAuth=FirebaseAuth.getInstance();

        adapter = new homeAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadTop10Data();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        adapter.setUpOnListener(this);
    }

    public void loadTop10Data(){
        Query query = FirebaseDatabase.getInstance().getReference("sketches").orderByChild("upvotes").limitToFirst(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
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
                            upvoters.add(new Vote(s.child("key").getValue().toString(), s.child("uid").getValue().toString()));

                    int voteStatus = 0;
                    if (ds.child("listOfUpvoters").hasChild(mAuth.getUid()))
                        voteStatus = 1;

                    ArrayList<Item> similarItems=new ArrayList<>();
                    if(ds.hasChild("itemArrayList"))
                        for(DataSnapshot s:ds.child("itemArrayList").getChildren())
                            similarItems.add(new Item(s.child("itemId").getValue().toString(),s.child("picUrl").getValue().toString(),((long) s.child("price").getValue())));
                    String key = ds.getKey();
                    list.add(new Model(uid,name,sketchUrl,mlUrl,upVotesNo,upvoters,time,voteStatus,similarItems,key));
                }
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                    if (list.isEmpty()) {
                        Toast.makeText(top10.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    public void upVoteClicked(Model model) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference upVotesRef = databaseReference.child("sketches").child(model.getKey()).child("listOfUpvoters");
        DatabaseReference sketchRef = databaseReference.child("sketches").child(model.getKey());

        Vote upvote = new Vote(model.getKey(), mAuth.getUid());

        upVotesRef.child(mAuth.getUid()).setValue(upvote).addOnCompleteListener(task -> sketchRef.get().addOnCompleteListener(task1 -> {
            HashMap<String, Object> data = new HashMap<>();
            if (task1.getResult().hasChild("listOfUpvoters"))
                data.put("upvotes", task1.getResult().child("listOfUpvoters").getChildrenCount());
            else data.put("upvotes", 0);
            sketchRef.updateChildren(data);
        }));
    }

    @Override
    public void onDesignClicked(Model model) {
        Intent intent = new Intent(this, FileUploadActivity.class);
        intent.putExtra("data", model);
        intent.putExtra("from", "home");
        startActivity(intent);
    }
}