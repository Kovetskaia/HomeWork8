package com.example.nastya.homework4;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewsContent extends AppCompatActivity {
    final int ADD = 0;
    final int DELETE = 1;
    TextView contentDate, contentDescription;
    ItemNews itemNews;
    MenuItem favourites;
    NewsDatabase db;
    FavouritesNewsDao favouritesNewsDao;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);

        contentDate = findViewById(R.id.contentDate);
        contentDescription = findViewById(R.id.contentDescription);

        itemNews = getIntent().getParcelableExtra(ItemNews.class.getSimpleName());

        setTitle(itemNews.getTitleNews());
        contentDate.setText(itemNews.getDateNews());
        contentDescription.setText(itemNews.getDescriptionNews());

        db = App.getInstance().getDatabase();
        favouritesNewsDao = db.favouritesNewsDao();
        id = itemNews.getId();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        favourites = menu.findItem(R.id.favourites);
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
                NewsFavouritesFragment.getInstance().changeListFavouritesNews(ADD, id);
                menuItem.setIcon(R.drawable.added);
                Toast.makeText(this, getString(R.string.addToFavourites), Toast.LENGTH_LONG).show();
            } else {
                NewsFavouritesFragment.getInstance().changeListFavouritesNews(DELETE, id);
                favouritesNewsDao.delete(favouritesNewsDao.getById(id));
                menuItem.setIcon(R.drawable.not_added);
                Toast.makeText(this, getString(R.string.deleteFromFavourites), Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
