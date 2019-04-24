package com.example.nastya.homework4.ui;

import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nastya.homework4.R;
import com.example.nastya.homework4.database.App;
import com.example.nastya.homework4.database.ItemNewsDao;
import com.example.nastya.homework4.database.NewsDatabase;
import com.example.nastya.homework4.network.Client;
import com.example.nastya.homework4.network.NewsService;
import com.example.nastya.homework4.network.ServerNewsItemDetails;
import com.example.nastya.homework4.network.ServerResponse;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NewsListFragment extends Fragment {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private List<ListItem> itemsList = new ArrayList<>();
    private Long oldDate;
    private LocalDate curDay;
    private LocalDate yesDay;
    private ItemNewsDao itemNewsDao;
    private MyAdapter myAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsService api;
    private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final String TAG = NewsListFragment.class.getSimpleName();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        curDay = LocalDate.now();
        yesDay = curDay.minusDays(1);

        NewsDatabase db = App.getInstance().getDatabase();
        itemNewsDao = db.newsDao();
        api = Client.getClientInstance().getMyDataApi();
        initData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        swipeRefreshLayout = new SwipeRefreshLayout(container.getContext());

        swipeRefreshLayout.addView(rootView);
        return swipeRefreshLayout;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter(itemsList, (position, news) -> {
            if (news.getDescriptionNews() != null) {
                startNewsContentActivity(news);
            } else {
                getContent(news.getId());
            }
        });

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
            swipeRefreshSetFalse();
            onCreateDialog(getString(R.string.titleConnectionError), getString(R.string.messageConnectionError));
        }

    }

    private boolean checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private void getDataFromServer() {
        mDisposable.add(api.getNewsList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ServerResponse<List<ItemNews>>>() {
                    @Override
                    public void onSuccess(ServerResponse<List<ItemNews>> listServerResponse) {
                        List<ItemNews> newsFromNetwork = listServerResponse.getPayload().subList(0, 100);

                        if (newsFromNetwork.size() != 0) {
                            if (needUpdate(newsFromNetwork)) {
                                groupByDate(newsFromNetwork);
                                oldDate = ((ItemNews) itemsList.get(itemsList.size() - 1)).getPublicationDate().getMilliseconds();
                                updateAdapter();
                                insertNewsToDB(newsFromNetwork);
                            } else {
                                swipeRefreshSetFalse();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCreateDialog(getString(R.string.titleRequestFailed), getString(R.string.messageRequestFailed));
                    }
                }));
    }

    private void getContentFromServer(ItemNews itemNews) {
        mDisposable.add(api.getDescription(itemNews.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ServerResponse<ServerNewsItemDetails>>() {
                    @Override
                    public void onSuccess(ServerResponse<ServerNewsItemDetails> serverNewsItemDetailsServerResponse) {
                        String description = serverNewsItemDetailsServerResponse.getPayload().getContent();
                        itemNews.setDescriptionNews(description);
                        startNewsContentActivity(itemNews);
                        insertContentToDB(itemNews);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCreateDialog(getString(R.string.titleRequestFailed), getString(R.string.messageRequestFailed));
                    }
                }));

    }

    private void getDataFromDB() {
        mDisposable.add(itemNewsDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listItems -> {
                    if (listItems.size() != 0) {
                        NewsListFragment.this.groupByDate(listItems);
                    }
                    NewsListFragment.this.updateAdapter();

                }, throwable -> Log.e(TAG, "Unable to get data", throwable)));

    }

    private void getContent(int id) {
        mDisposable.add(itemNewsDao.getById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemNews -> {
                    if (itemNews.getDescriptionNews() != null) {
                        startNewsContentActivity(itemNews);
                    } else {
                        if (checkConnection()) {
                            getContentFromServer(itemNews);
                        } else {
                            onCreateDialog(getString(R.string.titleConnectionError), getString(R.string.messageConnectionError));
                        }
                    }
                }));

    }

    private void insertNewsToDB(List<ItemNews> news) {
        mDisposable.add(itemNewsDao.insert(news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::deleteOldNewsFromDB,
                        error -> Log.e(TAG, "Unable to insert data", error)));

    }

    private void insertContentToDB(ItemNews news) {
        mDisposable.add(itemNewsDao.insertContent(news.getDescriptionNews(), news.getId())
                .subscribeOn(Schedulers.io())
                .subscribe());

    }

    private void deleteOldNewsFromDB() {
        mDisposable.add(itemNewsDao.delete(oldDate)
                .subscribeOn(Schedulers.io())
                .subscribe());

    }

    private boolean needUpdate(List<ItemNews> list) {
        if (itemsList.size() != 0) {
            long maxDate = ((ItemNews) itemsList.get(1)).getPublicationDate().getMilliseconds();
            for (ItemNews i : list) {
                if (i.getPublicationDate().getMilliseconds() > maxDate) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;

    }


    private void updateAdapter() {
        myAdapter.notifyDataSetChanged();
        swipeRefreshSetFalse();

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

        if (date.equals(curDay.toString())) {
            return getString(R.string.today);
        }
        if (date.equals(yesDay.toString())) {
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

    private void startNewsContentActivity(ItemNews news) {
        NewsListFragment.this.startActivity(NewsContentActivity.createIntent(NewsListFragment.this.getContext(), news));

    }

    private void swipeRefreshSetFalse() {
        swipeRefreshLayout.setRefreshing(false);

    }

}
