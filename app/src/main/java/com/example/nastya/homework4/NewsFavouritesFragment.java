package com.example.nastya.homework4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsFavouritesFragment extends Fragment {
    private static NewsFavouritesFragment newsFavouritesFragment;
    private List<ListItem> newsList = new ArrayList<>();
    private View rootView;

    public static NewsFavouritesFragment newInstance() {
        newsFavouritesFragment = new NewsFavouritesFragment();
        return newsFavouritesFragment;
    }

    public static NewsFavouritesFragment getInstance() {
        return newsFavouritesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    public void addNews(ItemNews favouriteItemNews) {
        newsList.add(new ItemNews(favouriteItemNews.getTitleNews(), favouriteItemNews.getDateNews(), favouriteItemNews.getDescriptionNews()));

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter(newsList, (position, news) -> {
            Intent intent = new Intent(getContext(), NewsContent.class);
            intent.putExtra(ItemNews.class.getSimpleName(), news);
            startActivity(intent);
        }));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
