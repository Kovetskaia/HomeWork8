package com.example.nastya.homework4;

import android.os.Bundle;
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

public class NewsListFragment extends Fragment {
    private List<ListItem> itemsList = new ArrayList<>();
    private View rootView;
    private LocalDate curDay;
    private LocalDate yesDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        curDay = LocalDate.now();
        yesDay = curDay.minusDays(1);

        NewsDatabase db = App.getInstance().getDatabase();
        NewsDao newsDao = db.newsDao();
        List<ItemNews> itemNewsList = newsDao.getAll();

        Map<LocalDate, List<ItemNews>> groupNews = toMap(itemNewsList);

        for (LocalDate date : groupNews.keySet()) {
            for (ItemNews itemNews : groupNews.get(date)) {
                itemsList.add(0, itemNews);
            }
            ItemDateGroup header = new ItemDateGroup(checkDate(date));
            itemsList.add(0, header);

        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter(itemsList, (position, news) -> {
            startActivity(NewsContentActivity.createIntent(getContext(), news));
        }));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
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
}
