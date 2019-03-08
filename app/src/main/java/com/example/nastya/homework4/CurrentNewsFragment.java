package com.example.nastya.homework4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CurrentNewsFragment extends Fragment{
    private List<News> newsList = new ArrayList<>();
    private List<ListItem> items = new ArrayList<>();
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addNews();

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
    private void addNews(){
//        for (int i = 0; i<2; i++) {
            newsList.add(new News(getString(R.string.Title1), "06.01.2018", getString(R.string.Description1)));
            newsList.add(new News(getString(R.string.Title1), "06.01.2018", getString(R.string.Description1)));
            newsList.add(new News(getString(R.string.Title2), getString(R.string.Date2), getString(R.string.Description2)));
            newsList.add(new News(getString(R.string.Title3), getString(R.string.Date3), getString(R.string.Description3)));
            newsList.add(new News(getString(R.string.Title4), getString(R.string.Date4), getString(R.string.Description4)));
            newsList.add(new News(getString(R.string.Title4), "00.02.2019", getString(R.string.Description4)));
            newsList.add(new News(getString(R.string.Title5), getString(R.string.Date5), getString(R.string.Description5)));
//        }

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
