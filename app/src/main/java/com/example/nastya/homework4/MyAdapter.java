package com.example.nastya.homework4;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

interface Listener {
    void onUserClick(int position, News n);
}

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private LayoutInflater inflater;

    private List<News> newsList;
    private Listener listener;

    MyAdapter(List<News> news, Listener listener){
        this.newsList = news;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater =
                LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_news, parent, false);

        final MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onUserClick(pos, newsList.get(pos));
            }
        });

        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            News news = newsList.get(position);
            holder.titleNews.setText(news.getTitleNews());
            holder.dateNews.setText(news.getDateNews());
            holder.descriptionNews.setText(news.getDescriptionNews());
        Log.d("myLogs", "bind, position = " + position);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

}
