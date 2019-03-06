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

public class CurrentNewsFragment extends Fragment{
    private List<News> newsList = new ArrayList<>();
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
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter(newsList, (position, news) -> {
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
        for (int i = 0; i<10; i++) {
            newsList.add(new News(getString(R.string.Title1), getString(R.string.Date1), getString(R.string.Description1)));
            newsList.add(new News(getString(R.string.Title2), getString(R.string.Date2), getString(R.string.Description2)));
            newsList.add(new News(getString(R.string.Title3), getString(R.string.Date3), getString(R.string.Description3)));
            newsList.add(new News(getString(R.string.Title4), getString(R.string.Date4), getString(R.string.Description4)));
            newsList.add(new News(getString(R.string.Title5), getString(R.string.Date5), getString(R.string.Description5)));
        }

    }


}
