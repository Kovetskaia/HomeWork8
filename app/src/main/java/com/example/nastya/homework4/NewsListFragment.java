package com.example.nastya.homework4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

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
    private List<ItemNews> itemsListForDelete = new ArrayList<>();
    private Long oldDate;
    private View rootView;
    private LocalDate curDay;
    private LocalDate yesDay;
    private NewsDatabase db;
    private NewsDao newsDao;
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        curDay = LocalDate.now();
        yesDay = curDay.minusDays(1);

        db = App.getInstance().getDatabase();
        newsDao = db.newsDao();

        mDisposable.add(newsDao.getAll()
//                .map(itemNewsList -> {
//                    if(itemNewsList.size() != 0) {
//
//                        //LocalDate localDate = new LocalDate(itemNewsList.get(0).getDateNews());
//                        long dateInMilliseconds = itemNewsList.get(0).getPublicationDate().getMilliseconds();
//                        itemsList.add(new ItemDateGroup(checkDate(dateFormat.print(dateInMilliseconds))));
//
//                        for (ItemNews i : itemNewsList) {
//
//                            if (i.getPublicationDate().getMilliseconds() == dateInMilliseconds){
//                                itemsList.add(i);
//                            } else {
//                                dateInMilliseconds = i.getPublicationDate().getMilliseconds();
//                               // localDate = new LocalDate(l);
//                                itemsList.add(new ItemDateGroup(checkDate(dateFormat.print(dateInMilliseconds))));
//                                itemsList.add(i);
//                            }
//                        }
//                        return itemsList;
//                    }else {
//                        itemsList.clear();
//                        return itemsList;
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ItemNews>>() {
                    @Override
                    public void accept(List<ItemNews> listItems) throws Exception {
                        Log.d("myLogs", "DB " + listItems);
                        itemsList.clear();
                        itemsList.addAll(listItems);
                        Log.d("myLogs", "itemsList " + itemsList);
                        startInsert();
                        //itemsListForDelete.addAll(listItems.subList(5,listItems.size()));
                        oldDate = ((ItemNews) itemsList.get(itemsList.size()-1)).getDate();
                        Log.d("myLogs", "itemsListForDelete " + oldDate);
                        startDelete();
//                        if(listItems.size()>100){
//                        itemsList.addAll(listItems.subList(0,99));
//                            startInsert();
//                        itemsListForDelete.addAll(listItems.subList(100,listItems.size()));
//                            getFavourites();
//                        }else {
//
//
//                        }
                    }
                }));



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter(itemsList, (position, news) ->
                NewsListFragment.this.startActivity(NewsContentActivity.createIntent(NewsListFragment.this.getContext(), news)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

     private void startInsert(){
        recyclerView.setAdapter(myAdapter);

    }


    private void startDelete() {
        mDisposable.add(newsDao.delete(oldDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
        Log.d("myLogs", "startDelete ");

    }


    private String checkDate(String date) {

        if (date.equals(curDay.toString())) {
            return getString(R.string.today);
        }
        if (date.equals(yesDay.toString())) {
            return getString(R.string.yesterday);
        }
        return date;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("myLogs", "onDestroyView ");
        mDisposable.clear();

    }
}
