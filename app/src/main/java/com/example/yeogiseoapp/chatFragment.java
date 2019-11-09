package com.example.yeogiseoapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;




public class chatFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    chatAdapter mAdapter;
    String em;
    List<chat> l = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        return v;
    }

    public void initChat() {
        initData();
        recyclerView = (RecyclerView)getView().findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        em = ((roomActivity)getActivity()).getEmail();

        // specify an adapter (see also next example)
        mAdapter = new chatAdapter(l, em);
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyItemInserted(l.size() -1);
    }

    public void initData() {
        chat c1 = new chat("tlawjd99@naver.com", "안녕??");
        chat c2 = new chat("aa", "너두 안녕??");
        l.add(c1);
        l.add(c2);
    }

    public void sendStr(String name, String txt)
    {
        chat c = new chat(name, txt);
        l.add(c);
        mAdapter.notifyItemInserted(l.size() -1);
    }
}
