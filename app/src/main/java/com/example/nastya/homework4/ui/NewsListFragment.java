package com.example.nastya.homework4.ui;

import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.nastya.homework4.R;
import com.example.nastya.homework4.database.App;
import com.example.nastya.homework4.database.ItemNewsDao;
import com.example.nastya.homework4.database.NewsDatabase;
import com.example.nastya.homework4.network.Client;
import com.example.nastya.homework4.network.NewsService;
import com.example.nastya.homework4.network.ServerResponse;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NewsListFragment extends Fragment {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private List<ListItem> itemsList = new ArrayList<>();
    private Long oldDate;
    private ItemNewsDao itemNewsDao;
    private MyAdapter myAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsService api;
    private ConnectivityManager connectivityManager;
    private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final String TAG = NewsListFragment.class.getSimpleName();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NewsDatabase db = App.getInstance().getDatabase();
        itemNewsDao = db.newsDao();
        api = Client.getClientInstance().getMyDataApi();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        return swipeRefreshLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter(itemsList, (position, news) -> startActivity(NewsContentActivity.createIntent(NewsListFragment.this.getContext(), news)));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(myAdapter);
        swipeRefreshLayout.setOnRefreshListener(this::initData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposable.clear();
    }

    private void initData() {
        if (checkConnection()) {
            getDataFromServer();
        } else {
            swipeRefreshLayout.setRefreshing(false);
            onCreateDialog(getString(R.string.titleConnectionError), getString(R.string.messageConnectionError));
        }
    }

    private void initConnectivityManager() {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        }
    }

    private boolean checkConnection() {
        initConnectivityManager();
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void getDataFromServer() {
        mDisposable.add(api.getNewsList()
                .map(ServerResponse::getPayload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemNews -> {
                    if (itemNews.size() != 0) {
                        groupByDate(itemNews);
                        oldDate = itemNews.get(100).getPublicationDate().getMilliseconds();
                        updateAdapter();
                        insertNewsToDB(itemNews.subList(0, 100));
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, throwable -> onCreateDialog(getString(R.string.titleRequestFailed), getString(R.string.messageRequestFailed))));
    }

    private void getDataFromDB() {
        mDisposable.add(itemNewsDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listItems -> {
                    if (listItems.size() != 0) {
                        groupByDate(listItems);
                    }
                    updateAdapter();

                }, throwable -> Log.e(TAG, "Unable to get data", throwable)));
    }

    private void insertNewsToDB(List<ItemNews> news) {
        mDisposable.add(itemNewsDao.insert(news)
                .andThen(itemNewsDao.delete(oldDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    private void updateAdapter() {
        myAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void groupByDate(List<ItemNews> listForGroup) {
        itemsList.clear();
        String dateInMilliseconds = dateFormat.print(listForGroup.get(0).getPublicationDate().getMilliseconds());
        itemsList.add(new ItemDateGroup(NewsListFragment.this.checkDate(dateInMilliseconds)));

        for (ItemNews i : listForGroup) {

            if (dateFormat.print(i.getPublicationDate().getMilliseconds()).equals(dateInMilliseconds)) {
                itemsList.add(i);
            } else {
                dateInMilliseconds = dateFormat.print(i.getPublicationDate().getMilliseconds());
                itemsList.add(new ItemDateGroup(NewsListFragment.this.checkDate(dateInMilliseconds)));
                itemsList.add(i);
            }
        }
    }

    private String checkDate(String date) {
        LocalDate currentDay = LocalDate.now();
        LocalDate yesterday = currentDay.minusDays(1);

        if (date.equals(currentDay.toString())) {
            return getString(R.string.today);
        }
        if (date.equals(yesterday.toString())) {
            return getString(R.string.yesterday);
        }
        return date;
    }

    private void onCreateDialog(String title, String message) {
        AlertDialog.Builder dialogWindow = new AlertDialog.Builder(getContext());
        dialogWindow.setTitle(title);
        dialogWindow.setMessage(message);
        dialogWindow.setIcon(android.R.drawable.ic_dialog_info);
        dialogWindow.setPositiveButton(R.string.ok,
                (dialog, which) -> {
                    if (itemsList.size() == 0) {
                        getDataFromDB();
                    }
                    dialog.cancel();
                });
        dialogWindow.setCancelable(false);
        dialogWindow.show();
    }

}
