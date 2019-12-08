package com.example.yeogiseoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.yeogiseoapp.data.GroupData;
import com.example.yeogiseoapp.data.GroupResponse;
import com.example.yeogiseoapp.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HallActivity extends AppCompatActivity {

    View layout;
    RoomAdapter adapter;
    String email, username, id;
    private SharedPreferences sp;
    private ServiceApi service = null;
    EditText groupName;
    public makegroupPopupFragment mf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        layout = findViewById(R.id.hall_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences("loggedinData", MODE_PRIVATE);
        service = RetrofitClient.getInstance().create(ServiceApi.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        email = sp.getString("loggedinEmail", null);
        username = sp.getString("loggedinUsername", null);
        id = sp.getString("loggedinId", null);
        ListView listview;

        adapter = new RoomAdapter();
        listview = (ListView)findViewById(R.id.listview);
        listview.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMakeGroupPopup();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                listViewItem item = (listViewItem)parent.getItemAtPosition(position);
                String room = item.getRoom();
                String info = item.getInfo();

                Intent intent = new Intent(HallActivity.this, roomActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("username", username);
                intent.putExtra("room", room);
                intent.putExtra("info", info);
                startActivity(intent);

            }
        });
    }

    public void openMakeGroupPopup(){
        makegroupPopupFragment dialog = makegroupPopupFragment.newInstance();
        mf = dialog;
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    private void makeRoom(final GroupData data) {
        service.groupMake(data).enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                GroupResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();
                String groupID = result.getGroupID();
                Toast.makeText(HallActivity.this, message, Toast.LENGTH_SHORT).show();
                if (code == 201) {
                    // 그룹 생성 성공
                    adapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher_foreground), data.getGroupName(), data.getGroupName()+" 입니다.");
                    Snackbar.make(layout, "방이 만들어졌습니다.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    adapter.notifyDataSetChanged();
                }
                else {
                    // 그룹 생성 실패
                    Log.d("group making failed", message);
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                Toast.makeText(HallActivity.this, "그룹 생성 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("그룹 생성 오류 발생", t.getMessage());
            }
        });
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
                sp.edit().remove("loggedinId").commit();
                Intent goback = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goback);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void mGroupOnClick(View v) {
        switch (v.getId()) {
            case R.id.popOkBtn:
                String gn = mf.getGroupName();
                makeRoom(new GroupData(id, gn));
                mf.dismissDialog();
                break;

            case R.id.popNoBtn:
                mf.dismissDialog();
                break;
        }
    }
}
