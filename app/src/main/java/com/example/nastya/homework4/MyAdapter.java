package com.example.nastya.homework4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

interface Listener {
    void onUserClick(int position, News n);
}

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
    LayoutInflater inflater;

    private List<News> events;
    private Listener listener;

    MyAdapter(List<News> news, Listener listener){
        this.events = news;
        this.listener = listener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater =
                LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_news, parent, false);

        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onUserClick(pos, events.get(pos));
            }
        });

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            News news = events.get(position);
            holder.titleNews.setText(news.getTitleNews());
            holder.dateNews.setText(news.getDateNews());
            holder.descriptionNews.setText(news.getDescriptionNews());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

}
