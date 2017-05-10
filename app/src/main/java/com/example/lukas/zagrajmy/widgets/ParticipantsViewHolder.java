package com.example.lukas.zagrajmy.widgets;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lukas.zagrajmy.R;

public class ParticipantsViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    ImageView imageView;

    ParticipantsViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.participant_name);
        imageView = (ImageView) itemView.findViewById(R.id.participant_photo);
    }
}