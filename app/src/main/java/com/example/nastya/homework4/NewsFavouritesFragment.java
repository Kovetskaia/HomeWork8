package com.example.nastya.homework4;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsFavouritesFragment extends Fragment {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private List<ListItem> newsList = new ArrayList<>();
    private List<FavouritesNews> currentFavouritesNews = new ArrayList<>();
    private View rootView;
    private NewsDao newsDao;
    private List<FavouritesNews> idToDelete = new ArrayList<>();
    private List<Long> idNews = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewsDatabase db = App.getInstance().getDatabase();
        newsDao = db.newsDao();
        FavouritesNewsDao favouritesNewsDao = db.favouritesNewsDao();

        mDisposable.add(favouritesNewsDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favouritesNews -> {
                    idToDelete.clear();
                    idToDelete.addAll(favouritesNews);
                    idToDelete.removeAll(currentFavouritesNews);

                    if (idToDelete.size() != 0) {
                        updateFavouritesNews(idToDelete, true);
                    }

                    currentFavouritesNews.removeAll(favouritesNews);

                    if (currentFavouritesNews.size() != 0) {
                        updateFavouritesNews(currentFavouritesNews, false);
                    }

                    currentFavouritesNews = favouritesNews;
                }
                )
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        return rootView;
    }

    private void setAdapter() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter(newsList, (position, news) -> NewsFavouritesFragment.this.startActivity(NewsContentActivity.createIntent(NewsFavouritesFragment.this.getContext(), news))));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void updateFavouritesNews(List<FavouritesNews> news, Boolean addOrDelete) {

        if (addOrDelete) {
            idNews.clear();
            for (FavouritesNews i : news) {
                idNews.add((long) i.getId());
            }
            newsDao.getByIds(idNews)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribe(new DisposableSingleObserver<List<ItemNews>>() {
                        @Override
                        public void onSuccess(List<ItemNews> itemNewsList) {
                            newsList.addAll(itemNewsList);
                            setAdapter();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        } else {
            newsList.removeAll(news);
            setAdapter();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}

