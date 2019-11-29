package com.example.yeogiseoapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.yeogiseoapp.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class HallActivity extends AppCompatActivity {

    Context context;
    RoomAdapter adapter;
    String sid, snickname;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences("loggedinData", MODE_PRIVATE);

        FloatingActionButton fab = findViewById(R.id.fab);
        sid = sp.getString("loggedinEmail", null);
        snickname = sp.getString("loggedinNickname", null);
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
                intent.putExtra("nickname", snickname);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hall, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_logout:
                Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show();
                sp.edit().remove("loggedinEmail").commit();
                sp.edit().remove("loggedinPassword").commit();
                sp.edit().remove("loggedinNickname").commit();
                Intent goback = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goback);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
