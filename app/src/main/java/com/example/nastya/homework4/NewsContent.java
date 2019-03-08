package com.example.nastya.homework4;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewsContent extends AppCompatActivity {
    TextView contentDate, contentDescription;
    ItemNews itemNews;
    MenuItem favourites;

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        favourites = menu.findItem(R.id.favourites);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.favourites) {
            NewsFavouritesFragment.getInstance().addNews(itemNews);
            Toast.makeText(this, "Новость добавлена в избранное", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
