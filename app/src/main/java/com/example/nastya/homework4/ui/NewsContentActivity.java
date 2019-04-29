package com.example.nastya.homework4.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nastya.homework4.R;
import com.example.nastya.homework4.database.App;
import com.example.nastya.homework4.database.FavouritesNewsDao;
import com.example.nastya.homework4.database.ItemNewsDao;
import com.example.nastya.homework4.database.NewsDatabase;
import com.example.nastya.homework4.network.Client;
import com.example.nastya.homework4.network.NewsService;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewsContentActivity extends AppCompatActivity {
    private FavouritesNewsDao favouritesNewsDao;
    private FavouritesNews currentNews;
    private int id;
    private Boolean starVisible = false;
    private static final String TAG = NewsContentActivity.class.getSimpleName();
    private ItemNewsDao itemNewsDao;
    private NewsService api;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private TextView contentDescription;
    private ConnectivityManager connectivityManager;
    private DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MMMM-dd");

    public static Intent createIntent(Context context, ItemNews itemNews) {
        return new Intent(context, NewsContentActivity.class)
                .putExtra(ItemNews.class.getSimpleName(), itemNews);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.news_content);
        TextView contentDate = findViewById(R.id.contentDate);
        contentDescription = findViewById(R.id.contentDescription);

        NewsDatabase db = App.getInstance().getDatabase();
        itemNewsDao = db.newsDao();
        api = Client.getClientInstance().getMyDataApi();

        ItemNews news = (ItemNews) getIntent().getSerializableExtra(ItemNews.class.getSimpleName());
        id = news.getId();

        getContent(news);

        contentDate.setText(dateFormat.print(news.getPublicationDate().getMilliseconds()));
        setTitle(news.getText());

    }

    @Override
    protected void onStart() {
        super.onStart();
        NewsDatabase db = App.getInstance().getDatabase();
        favouritesNewsDao = db.favouritesNewsDao();

        mDisposable.add(favouritesNewsDao.getById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favouritesNews -> {
                    starVisible = true;
                    currentNews = favouritesNews;
                }, throwable -> starVisible = false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem favourites = menu.findItem(R.id.favourites);
        if (starVisible) {
            favourites.setIcon(R.drawable.added);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.favourites) {
            if (!starVisible) {

                currentNews = new FavouritesNews(id);
                mDisposable.add(favouritesNewsDao.insert(currentNews)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    Toast.makeText(NewsContentActivity.this, getString(R.string.addedToFavourites), Toast.LENGTH_LONG).show();
                                    menuItem.setIcon(R.drawable.added);
                                    starVisible = true;
                                }, throwable -> Toast.makeText(NewsContentActivity.this, getString(R.string.notAddedToFavourites), Toast.LENGTH_LONG).show()
                        ));
            } else {

                mDisposable.add(favouritesNewsDao.delete(currentNews)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Toast.makeText(NewsContentActivity.this, getString(R.string.deletedFromFavourites), Toast.LENGTH_LONG).show();
                            menuItem.setIcon(R.drawable.not_added);
                            starVisible = false;
                        }, throwable -> Toast.makeText(NewsContentActivity.this, getString(R.string.notDeletedFromFavourites), Toast.LENGTH_LONG).show()));

            }
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void getContent(ItemNews news) {
        mDisposable.add(itemNewsDao.getById(news.getId())
                .doOnComplete(() -> {
                    itemNewsDao.insertOneNews(news);
                })
                .defaultIfEmpty(news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemNews -> {
                    if (itemNews.getDescriptionNews() != null) {
                        setContentInTextView(itemNews.getDescriptionNews());
                    } else {
                        if (checkConnection()) {
                            getContentFromServer(itemNews.getId());
                        } else {
                            onCreateDialog(getString(R.string.titleConnectionError), getString(R.string.messageConnectionError));

                        }
                    }
                }, throwable -> Log.e(TAG, "Unable to get data", throwable)));
    }

    private void getContentFromServer(int id) {
        mDisposable.add(api.getDescription(id)
                .map(serverNewsItemDetailsServerResponse -> serverNewsItemDetailsServerResponse.getPayload().getContent())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    setContentInTextView(s);
                    insertContentToDB(s, id);
                }, throwable -> onCreateDialog(getString(R.string.titleRequestFailed), getString(R.string.messageRequestFailed))));
    }

    private void insertContentToDB(String content, int id) {
        mDisposable.add(itemNewsDao.insertContent(content, id)
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    private void setContentInTextView(String content) {
        CharSequence styledString = Html.fromHtml(content);
        contentDescription.setText(styledString);
    }

    private void initConnectivityManager() {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        }
    }

    private boolean checkConnection() {
        initConnectivityManager();
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void onCreateDialog(String title, String message) {
        AlertDialog.Builder dialogWindow = new AlertDialog.Builder(this);
        dialogWindow.setTitle(title);
        dialogWindow.setMessage(message);
        dialogWindow.setIcon(android.R.drawable.ic_dialog_info);
        dialogWindow.setPositiveButton(R.string.ok,
                (dialog, which) -> {
                    dialog.cancel();
                    finish();
                });
        dialogWindow.setCancelable(false);
        dialogWindow.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
