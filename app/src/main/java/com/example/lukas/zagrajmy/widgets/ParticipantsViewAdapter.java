package com.example.lukas.zagrajmy.widgets;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lukas.zagrajmy.R;
import com.example.lukas.zagrajmy.model.Participant;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

public class ParticipantsViewAdapter extends RecyclerView.Adapter<ParticipantsViewHolder> {

    List<Participant> list = Collections.emptyList();
    Context context;
String imagePath;
    public ParticipantsViewAdapter(List<Participant> list, Context context, String imagePath) {
        this.list = list;
        this.context = context;
        this.imagePath = imagePath;
    }

    @Override
    public ParticipantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant, parent, false);
        ParticipantsViewHolder holder = new ParticipantsViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(ParticipantsViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());

        Picasso.with(context)
                .load(imagePath + list.get(position).getId())
                .memoryPolicy(NO_CACHE, NO_STORE)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Participant data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Participant data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

}