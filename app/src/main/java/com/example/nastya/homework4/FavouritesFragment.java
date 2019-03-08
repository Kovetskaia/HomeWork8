package com.example.nastya.homework4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavouritesFragment extends Fragment {
    private List<News> newsList = new ArrayList<>();
    private List<ListItem> items = new ArrayList<>();
    private static FavouritesFragment favouritesFragment;
    private View rootView;
    MyAdapter adapter;


    public static FavouritesFragment newInstance(){
        favouritesFragment = new FavouritesFragment();
        return favouritesFragment;
    }

    public static FavouritesFragment getInstance(){
        return favouritesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        adapter = (new MyAdapter(newsList, (position, news) -> {
//            Intent intent = new Intent(getContext(), ContentNews.class);
//            intent.putExtra(News.class.getSimpleName(), news);
//            startActivity(intent);
//        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    public void addNews(News favouriteNews){
        newsList.add(new News(favouriteNews.getTitleNews(), favouriteNews.getDateNews(), favouriteNews.getDescriptionNews()));
        Map<String, List<News>> groupNews = toMap(newsList);

        for (String date : groupNews.keySet()) {
            DateGroup header = new DateGroup(date);
            items.add(header);
            for (News news : groupNews.get(date)) {
                News item = new News(news.getTitleNews(), news.getDateNews(), news.getDescriptionNews());
                items.add(item);
            }
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyItemInserted(newsList.size()-1);
        recyclerView.setAdapter(new MyAdapter(items, (position, news) -> {
            Intent intent = new Intent(getContext(), ContentNews.class);
            intent.putExtra(News.class.getSimpleName(), news);
            startActivity(intent);
        }));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
    private Map<String, List<News>> toMap(List<News> listNews) {
        Map<String, List<News>> map = new TreeMap<>();
        for (News news : listNews) {
            List<News> value = map.get(news.getDateNews());
            if (value == null) {
                value = new ArrayList<>();
                map.put(news.getDateNews(), value);
            }
            value.add(news);
        }
        return map;
    }
}
