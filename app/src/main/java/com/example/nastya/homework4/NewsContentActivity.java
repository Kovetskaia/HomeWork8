package com.example.nastya.homework4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class NewsContentActivity extends AppCompatActivity {
    final int ADD = 0;
    final int DELETE = 1;
    ItemNews itemNews;
    FavouritesNewsDao favouritesNewsDao;
    private static Fragment fragment;
    int id;

    public static Intent createIntent(Context context, ItemNews news) {
        fragment = ((MainActivity) context).getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.pager + ":" + 1);
        return new Intent(context, NewsContentActivity.class)
                .putExtra(ItemNews.class.getSimpleName(), news);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);

        TextView contentDate = findViewById(R.id.contentDate);
        TextView contentDescription = findViewById(R.id.contentDescription);

        itemNews = (ItemNews) getIntent().getSerializableExtra(ItemNews.class.getSimpleName());

        setTitle(itemNews.getTitleNews());
        contentDate.setText(itemNews.getDateNews());
        contentDescription.setText(itemNews.getDescriptionNews());

        NewsDatabase db = App.getInstance().getDatabase();
        favouritesNewsDao = db.favouritesNewsDao();
        id = itemNews.getId();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem favourites = menu.findItem(R.id.favourites);
        if (favouritesNewsDao.getById(id) != null) {
            favourites.setIcon(R.drawable.added);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.favourites) {
            if (favouritesNewsDao.getById(id) == null) {
                favouritesNewsDao.insert(new FavouritesNews(id));
                if (fragment != null) {
                    ((NewsFavouritesFragment) fragment).changeListFavouritesNews(ADD, id);
                }
                menuItem.setIcon(R.drawable.added);
                Toast.makeText(this, getString(R.string.addToFavourites), Toast.LENGTH_LONG).show();
            } else {
                if (fragment != null) {
                    ((NewsFavouritesFragment) fragment).changeListFavouritesNews(DELETE, id);
                }
                favouritesNewsDao.delete(favouritesNewsDao.getById(id));
                menuItem.setIcon(R.drawable.not_added);
                Toast.makeText(this, getString(R.string.deleteFromFavourites), Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
