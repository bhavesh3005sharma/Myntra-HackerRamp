package com.hackathon.myntra_hackerramp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hackathon.myntra_hackerramp.FileUploadActivity;
import com.hackathon.myntra_hackerramp.R;
import com.hackathon.myntra_hackerramp.model.Model;

import java.util.List;

public class homeAdapter extends RecyclerView.Adapter<homeAdapter.holder> {
    Context context;
    List<Model> list;
    listener mListener;

    public homeAdapter(Context context,List<Model> list){
        this.context=context;
        this.list=list;
    }

    public void setUpOnListener(listener mListener) {
        this.mListener = mListener;
    }

    public interface listener{
        void upVoteClicked(Model model);
        void onDesignClicked(Model model);
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        Model model=list.get(position);
        holder.username.setText(model.getUsername());
        holder.timeStamp.setText(model.getTimeStampStr());
        holder.upvotesNo.setText(String.valueOf(model.getUpvotes()));
        holder.price1.setText(String.valueOf(model.getItemArrayList().get(0).getPrice()));
        holder.price2.setText(String.valueOf(model.getItemArrayList().get(1).getPrice()));
        holder.price3.setText(String.valueOf(model.getItemArrayList().get(2).getPrice()));

        if (model.getVoteStatus() == 1) {
            holder.upVote.setImageResource(R.drawable.ic_upvote_filled);
        }

        if(model.getDesignUrl()!=null) Glide.with(context).load(model.getDesignUrl()).into(holder.design);
        if(model.getMlUrl()!=null) Glide.with(context).load(model.getDesignUrl()).into(holder.ml);
        if(model.getItemArrayList().get(0).getPicUrl()!=null) Glide.with(context).load(model.getDesignUrl()).into(holder.item1);
        if(model.getItemArrayList().get(1).getPicUrl()!=null) Glide.with(context).load(model.getDesignUrl()).into(holder.item2);
        if(model.getItemArrayList().get(2).getPicUrl()!=null) Glide.with(context).load(model.getDesignUrl()).into(holder.item3);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FileUploadActivity.class);
                intent.putExtra("from", "home");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public static class holder extends RecyclerView.ViewHolder{
        TextView username,timeStamp,upvotesNo,price1,price2,price3;
        ImageView design,ml,item1,item2,item3,upVote;


        public holder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.textViewUsername);
            timeStamp=itemView.findViewById(R.id.textViewDateTime);
            price1=itemView.findViewById(R.id.price1);
            price2=itemView.findViewById(R.id.price2);
            price3=itemView.findViewById(R.id.price3);
            upvotesNo=itemView.findViewById(R.id.upVoteNumber);
            design=itemView.findViewById(R.id.design);
            ml=itemView.findViewById(R.id.mlitem);
            item1=itemView.findViewById(R.id.item1);
            item2=itemView.findViewById(R.id.item2);
            item3=itemView.findViewById(R.id.item3);
            upVote=itemView.findViewById(R.id.upVote);
        }
    }
}
