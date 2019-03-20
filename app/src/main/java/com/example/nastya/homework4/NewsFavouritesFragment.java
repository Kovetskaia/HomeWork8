package com.example.nastya.homework4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsFavouritesFragment extends Fragment {
    private Map<Integer, ListItem> mapNews = new HashMap<>();
    private List<ListItem> newsList = new ArrayList<>();
    private View rootView;
    private NewsDao newsDao;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int idForAdd = intent.getIntExtra("id_add", -1);
            int idForDelete = intent.getIntExtra("id_delete", -1);
            if (idForAdd != -1) {
                ItemNews itemNews = newsDao.getById(idForAdd);
                mapNews.put(idForAdd, itemNews);
                newsList.add(itemNews);
                setAdapter();
            }
            if (idForDelete != -1) {
                newsList.remove(mapNews.get(idForDelete));
                setAdapter();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewsDatabase db = App.getInstance().getDatabase();
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

    private void setAdapter() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter(newsList, (position, news) -> {

            startActivity(NewsContentActivity.createIntent(getContext(), news));
        }));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(broadcastReceiver, new IntentFilter("news_state_change"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }
}
