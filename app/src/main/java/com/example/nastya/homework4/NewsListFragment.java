package com.example.nastya.homework4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListFragment extends Fragment {
    private List<ListItem> itemsList = new ArrayList<>();
    private View rootView;
    private Calendar calendar;
    private int curDay;
    private int curMonth;
    private int curYear;
    private int yesDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        curDay = calendar.get(Calendar.DAY_OF_MONTH);
        curMonth = calendar.get(Calendar.MONTH) + 1;
        curYear = calendar.get(Calendar.YEAR);
        yesDay = curDay - 1;

        NewsDatabase db = App.getInstance().getDatabase();
        NewsDao newsDao = db.newsDao();
        List<ItemNews> itemNewsList = newsDao.getAll();

        Map<Date, List<ItemNews>> groupNews = toMap(itemNewsList);

        for (Date date : groupNews.keySet()) {
            for (ItemNews itemNews : groupNews.get(date)) {
                ItemNews item = new ItemNews(itemNews.getId(), itemNews.getTitleNews(), itemNews.getDateNews(), itemNews.getDescriptionNews());
                itemsList.add(0, item);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String dateToString = dateFormat.format(date);
            ItemDateGroup header = new ItemDateGroup(checkDate(date, dateToString));
            itemsList.add(0, header);

        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter(itemsList, (position, news) -> {
            Intent intent = new Intent(getContext(), NewsContent.class);
            intent.putExtra(ItemNews.class.getSimpleName(), news);
            startActivity(intent);
        }));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private Map<Date, List<ItemNews>> toMap(List<ItemNews> listNews) {
        List<ItemNews> newsGroup = null;
        String dateNews;
        Date date = null;
        Map<Date, List<ItemNews>> map = new TreeMap<>();
        for (ItemNews itemNews : listNews) {
            dateNews = itemNews.getDateNews();
            try {
                date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(dateNews);
                newsGroup = map.get(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (newsGroup == null) {
                newsGroup = new ArrayList<>();
                map.put(date, newsGroup);
            }
            newsGroup.add(itemNews);
        }
        return map;
    }

    private String checkDate(Date date, String dateNews) {
        calendar.setTime(date);

        if (curMonth == calendar.get(Calendar.MONTH) + 1 & curYear == calendar.get(Calendar.YEAR)) {
            if (curDay == calendar.get(Calendar.DAY_OF_MONTH)) {
                return getString(R.string.today);
            }
            if (yesDay == calendar.get(Calendar.DAY_OF_MONTH)) {
                return getString(R.string.yesterday);
            }
        }
        return dateNews;
    }
}
