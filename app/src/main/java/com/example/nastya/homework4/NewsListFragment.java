package com.example.nastya.homework4;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsListFragment extends Fragment {
    private List<ListItem> itemsList = new ArrayList<>();
    private View rootView;
    private LocalDate curDay;
    private LocalDate yesDay;
    private List<ItemNews> itemNewsList;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curDay = LocalDate.now();
        yesDay = curDay.minusDays(1);
        NewsDatabase db = App.getInstance().getDatabase();
        NewsDao newsDao = db.newsDao();
        mDisposable.add(newsDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemNews -> {
                    itemNewsList = itemNews;
                    NewsListFragment.this.startInsert();
                }));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

     private void startInsert(){
        Map<LocalDate, List<ItemNews>> groupNews = toMap(itemNewsList);

        for (LocalDate date : groupNews.keySet()) {
            for (ItemNews i : groupNews.get(date)) {
                itemsList.add(0, i);
            }
            ItemDateGroup header = new ItemDateGroup(checkDate(date));
            itemsList.add(0, header);

        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter(itemsList, (position, news) -> NewsListFragment.this.startActivity(NewsContentActivity.createIntent(NewsListFragment.this.getContext(), news))));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private Map<LocalDate, List<ItemNews>> toMap(List<ItemNews> listNews) {
        List<ItemNews> newsGroup;
        String dateNews;
        LocalDate date;
        Map<LocalDate, List<ItemNews>> map = new TreeMap<>();
        for (ItemNews itemNews : listNews) {
            dateNews = itemNews.getDateNews();
            date = LocalDate.parse(dateNews);
            newsGroup = map.get(date);

            if (newsGroup == null) {
                newsGroup = new ArrayList<>();
                map.put(date, newsGroup);
            }
            newsGroup.add(itemNews);
        }
        return map;
    }

    private String checkDate(LocalDate date) {

        if (date.equals(curDay)) {
            return getString(R.string.today);
        }
        if (date.equals(yesDay)) {
            return getString(R.string.yesterday);
        }
        return date.toString();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
