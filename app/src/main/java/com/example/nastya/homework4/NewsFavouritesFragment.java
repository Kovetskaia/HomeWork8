package com.example.nastya.homework4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsFavouritesFragment extends Fragment {
    private static NewsFavouritesFragment newsFavouritesFragment;
    private Map<Integer, ListItem> mapNews = new HashMap<>();
    private List<ListItem> newsList = new ArrayList<>();
    private View rootView;
    private NewsDatabase db;
    private NewsDao newsDao;

    static NewsFavouritesFragment newInstance() {
        newsFavouritesFragment = new NewsFavouritesFragment();
        return newsFavouritesFragment;
    }

    static NewsFavouritesFragment getInstance() {
        return newsFavouritesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = App.getInstance().getDatabase();
        newsDao = db.newsDao();
        FavouritesNewsDao favouritesNewsDao = db.favouritesNewsDao();
        List<FavouritesNews> idNews = favouritesNewsDao.getAll();

        for (FavouritesNews i : idNews) {
            int id = i.getIdFavourites();
            ItemNews itemNews = newsDao.getById(id);
            mapNews.put(id, itemNews);
            newsList.add(itemNews);
        }
        if (newsList.size() != 0) {
            setAdapter();
        }
    }

    void changeListFavouritesNews(int change, int id) {

        if (change == 0) {
            ItemNews itemNews = newsDao.getById(id);
            mapNews.put(id, itemNews);
            newsList.add(itemNews);
        }
        if (change == 1) {
            newsList.remove(mapNews.get(id));
        }

        if (newsList.size() != 0) {
            setAdapter();
        }
    }

    private void setAdapter() {
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
