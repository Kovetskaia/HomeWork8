package com.example.nastya.homework4;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    static final int COUNT_PAGES = 2;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        MyFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Последние " : "Избранное";
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new CurrentNewsFragment() : FavouritesFragment.newInstance();
        }

        @Override
        public int getCount() {
            return COUNT_PAGES;
        }
    }
}