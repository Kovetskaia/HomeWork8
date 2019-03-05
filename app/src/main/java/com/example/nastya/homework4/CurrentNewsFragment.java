package com.example.nastya.homework4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CurrentNewsFragment extends Fragment{
    private static final String ARGUMENT_DOCUMENT_NUMBER = "document_number";
    private int documentNumber;
    List<News> news = new ArrayList<>();
    View rootView;

//    static CurrentNewsFragment newInstance() {
//        CurrentNewsFragment currentNewsFragment = new CurrentNewsFragment();
////        Bundle arguments = new Bundle();
////        arguments.putInt(ARGUMENT_DOCUMENT_NUMBER, number);
////        currentNewsFragment.setArguments(arguments);
//        return currentNewsFragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //получаем номер документа
//        documentNumber = getArguments().getInt(ARGUMENT_DOCUMENT_NUMBER) + 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pager, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInitialData();
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter(news, new Listener() {
            @Override
            public void onUserClick(int position, News n) {
                Toast.makeText(getContext(), "Что-то нажали" + position, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(), ContentNews.class);
                intent.putExtra(News.class.getSimpleName(), n);
                startActivity(intent);
            }
        }));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


//        TextView addPage = view.findViewById(R.id.newPage);
//        addPage.setText(String.format(getString(R.string.page), documentNumber));

    }
    private void setInitialData(){
        for (int i = 0; i<10; i++) {
            news.add(new News("News 1", "10.01.2019", "11111"));
            news.add(new News("News 2", "11.01.2019", "22222"));
            news.add(new News("News 3", "12.01.2019", "33333"));
            news.add(new News("News 4", "13.01.2019", "44444"));
            news.add(new News("News 5", "14.01.2019", "55555"));
        }

    }


}
