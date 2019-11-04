package com.example.yeogiseoapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class HallActivity extends AppCompatActivity {

    Context context;
    RoomAdapter adapter;
    String sid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        sid = getIntent().getStringExtra("id");
        ListView listview;

        adapter = new RoomAdapter();
        listview = (ListView)findViewById(R.id.listview);
        listview.setAdapter(adapter);
        context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRoom(view, context);
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                listViewItem item = (listViewItem)parent.getItemAtPosition(position);
                String room = item.getRoom();
                String info = item.getInfo();

                Intent intent = new Intent(HallActivity.this, roomActivity.class);
                intent.putExtra("id", sid);

                intent.putExtra("room", room);
                intent.putExtra("info", info);
                startActivity(intent);

            }
        });
    }
    private void makeRoom(View v, Context con) {
        adapter.addItem(ContextCompat.getDrawable(con, R.drawable.ic_launcher_foreground), "Room"+adapter.getCount(), "Room"+adapter.getCount()+" 입니다.");
        Snackbar.make(v, "방이 만들어졌습니다.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        adapter.notifyDataSetChanged();
    }
}
