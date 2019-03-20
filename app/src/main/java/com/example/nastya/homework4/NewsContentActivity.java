package com.example.nastya.homework4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NewsContentActivity extends AppCompatActivity {
    ItemNews itemNews;
    FavouritesNewsDao favouritesNewsDao;
    int id;

    public static Intent createIntent(Context context, ItemNews news) {
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
            Intent intent = new Intent();
            intent.setAction("news_state_change");
            if (favouritesNewsDao.getById(id) == null) {
                favouritesNewsDao.insert(new FavouritesNews(id));

                intent.putExtra("id_add", id);

                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                menuItem.setIcon(R.drawable.added);
                Toast.makeText(this, getString(R.string.addToFavourites), Toast.LENGTH_LONG).show();
            } else {
                intent.putExtra("id_delete", id);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                favouritesNewsDao.delete(favouritesNewsDao.getById(id));
                menuItem.setIcon(R.drawable.not_added);
                Toast.makeText(this, getString(R.string.deleteFromFavourites), Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
