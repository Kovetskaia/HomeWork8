package com.example.nastya.homework4;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ContentNews extends AppCompatActivity {
    TextView contentDate, contentDescription;
    News news;
    MenuItem favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_news);

        contentDate = findViewById(R.id.contentDate);
        contentDescription = findViewById(R.id.contentDescription);

        news = getIntent().getParcelableExtra(News.class.getSimpleName());

        setTitle(news.getTitleNews());
        contentDate.setText(news.getDateNews());
        contentDescription.setText(news.getDescriptionNews());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        favourites = menu.findItem(R.id.favourites);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.favourites) {

            Toast.makeText(this,"Новость добавлена в избранное" + news.getDescriptionNews(), Toast.LENGTH_LONG).show();
            return true;
        }
          return super.onOptionsItemSelected(menuItem);
    }

}
