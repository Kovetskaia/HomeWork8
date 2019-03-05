package com.example.nastya.homework4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavouritesFragment extends Fragment {
    private static final String ARGUMENT_DOCUMENT_NUMBER = "document_number";
    private News news;
    String TAG = "myLogs";

//    static FavouritesFragment newInstance(News news) {
//        FavouritesFragment favouritesFragment = new FavouritesFragment();
//        Bundle arguments = new Bundle();
//        arguments.putParcelable(ARGUMENT_DOCUMENT_NUMBER, news);
//        Log.d("myLogs", "newInstance " + news.getDescriptionNews());
//        favouritesFragment.setArguments(arguments);
//        return favouritesFragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        news = getArguments().getParcelable(ARGUMENT_DOCUMENT_NUMBER);
//        Log.d("myLogs", "onCreate " + news.getDescriptionNews());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d("myLogs", "onCreateView " + news.getDescriptionNews());
        return inflater.inflate(R.layout.favourites_fragment, container, false);
    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
////        TextView textView = new TextView(getActivity());
////        textView.setText(news.getDescriptionNews());
//        Log.d("myLogs", "onViewCreated " + news.getDescriptionNews());
//        TextView test = view.findViewById(R.id.test);
//        test.setText(news.getDescriptionNews());
//
//     //   LinearLayout linearLayout = view.findViewById(R.id.layoutFavourites);
//        //Log.d(TAG, "lin " + linearLayout);
//     //   linearLayout.addView(textView);
//
//    }
}
