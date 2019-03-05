package com.example.nastya.homework4;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView titleNews;
        final TextView dateNews;
        final TextView descriptionNews;

    MyViewHolder(View itemView) {
            super(itemView);
            titleNews = itemView.findViewById(R.id.titleNews);
            dateNews = itemView.findViewById(R.id.dateNews);
            descriptionNews = itemView.findViewById(R.id.descriptionNews);
        }
    }
