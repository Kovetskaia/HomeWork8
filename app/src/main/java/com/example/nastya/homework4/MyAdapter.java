package com.example.nastya.homework4;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

interface Listener {
    void onUserClick(int position, News n);
}

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater inflater;

    private List<ListItem> newsList;
    private Listener listener;

    MyAdapter(List<ListItem> news, Listener listener){
        this.newsList = news;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListItem.TYPE_DATE: {
                view = inflater.inflate(R.layout.item_date_group, parent, false);
                return new DataGroupViewHolder(view);
            }
            case ListItem.TYPE_NEWS: {
                view = inflater.inflate(R.layout.item_news, parent, false);
                NewsViewHolder holder = new NewsViewHolder(view);
                view.setOnClickListener(v -> {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        listener.onUserClick(pos, (News) newsList.get(pos));
                    }
                });

                return holder;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }




    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ListItem.TYPE_DATE: {
                DateGroup dateGroup = (DateGroup) newsList.get(position);
                ((DataGroupViewHolder) holder).txt_title.setText(dateGroup.getDate());
                break;
            }
            case ListItem.TYPE_NEWS: {
                News news = (News) newsList.get(position);
                ((NewsViewHolder) holder).titleNews.setText(news.getTitleNews());
                ((NewsViewHolder) holder).dateNews.setText(news.getDateNews());
                ((NewsViewHolder) holder).descriptionNews.setText(news.getDescriptionNews());
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }

//            News news = newsList.get(position);
//            holder.titleNews.setText(news.getTitleNews());
//            holder.dateNews.setText(news.getDateNews());
//            holder.descriptionNews.setText(news.getDescriptionNews());
//        Log.d("myLogs", "bind, position = " + position);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return newsList.get(position).getType();
    }

    private class NewsViewHolder extends RecyclerView.ViewHolder {
        final TextView titleNews;
        final TextView dateNews;
        final TextView descriptionNews;

        NewsViewHolder(View itemView) {
            super(itemView);
            titleNews = itemView.findViewById(R.id.titleNews);
            dateNews = itemView.findViewById(R.id.dateNews);
            descriptionNews = itemView.findViewById(R.id.descriptionNews);
        }
    }

    private static class DataGroupViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title;

        DataGroupViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.dateGroup);
        }

    }

}
