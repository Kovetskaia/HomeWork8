package com.example.nastya.homework4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class CurrentNewsFragment extends Fragment{
    private List<News> newsList = new ArrayList<>();
    private List<ListItem> items = new ArrayList<>();
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
        curMonth = calendar.get(Calendar.MONTH)+1;
        curYear = calendar.get(Calendar.YEAR);
        yesDay = curDay - 1;
        addNews();

        Map<Date, List<News>> groupNews = toMap(newsList);

        for (Date date : groupNews.keySet()) {
            for (News news : groupNews.get(date)) {
                News item = new News(news.getTitleNews(), news.getDateNews(), news.getDescriptionNews());
                items.add(0, item);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String dateToString = dateFormat.format(date);
            DateGroup header = new DateGroup(checkDate(date, dateToString));
            items.add(0, header);

        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter(items, new Listener() {
            @Override
            public void onUserClick(int position, News n) {
                Intent intent = new Intent(getContext(), ContentNews.class);
                intent.putExtra(News.class.getSimpleName(), n);
                startActivity(intent);
            }
        }));
//            Intent intent = new Intent(getContext(), ContentNews.class);
//            intent.putExtra(News.class.getSimpleName(), news);
//            startActivity(intent);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
    private void addNews(){
//        for (int i = 0; i<2; i++) {
            newsList.add(new News(getString(R.string.Title1), "06.01.2018", getString(R.string.Description1)));
            newsList.add(new News(getString(R.string.Title1), "07.03.2019", getString(R.string.Description1)));
            newsList.add(new News(getString(R.string.Title2), getString(R.string.Date2), getString(R.string.Description2)));
            newsList.add(new News(getString(R.string.Title3), getString(R.string.Date3), getString(R.string.Description3)));
            newsList.add(new News(getString(R.string.Title4), getString(R.string.Date4), getString(R.string.Description4)));
            newsList.add(new News(getString(R.string.Title4), "08.03.2019", getString(R.string.Description4)));
            newsList.add(new News(getString(R.string.Title5), getString(R.string.Date5), getString(R.string.Description5)));
//        }

    }

    private Map<Date, List<News>> toMap(List<News> listNews) {
        List<News> value = null;
        String dateNews;
        Date date = null;
        Map<Date, List<News>> map = new TreeMap<>();
        for (News news : listNews) {
            dateNews = news.getDateNews();
            try {
                date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(dateNews);
               // value = map.get(checkDate(date, dateNews));
                value = map.get(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (value == null) {
                value = new ArrayList<>();
                map.put(date, value);
            }
            value.add(news);
        }
        return map;
    }

    private String checkDate(Date date, String dateNews){
            calendar.setTime(date);

        if(curMonth == calendar.get(Calendar.MONTH)+1 & curYear == calendar.get(Calendar.YEAR)){
            if(curDay == calendar.get(Calendar.DAY_OF_MONTH)){
                return "Сегодня";
            }
            if(yesDay == calendar.get(Calendar.DAY_OF_MONTH)){
                return "Вчера";
            }
        }
        return dateNews;
    }
}
