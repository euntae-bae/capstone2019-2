package com.example.yeogiseoapp;

import android.content.ClipData;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class roomActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    String id, room, info;
    private static final int REQUEST_CODE = 200;
    ArrayList<Uri> uris = new ArrayList<Uri>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        chatFragment cf = (chatFragment) getSupportFragmentManager().findFragmentById(R.id.chat_content);
        cf.initChat();
        room = intent.getStringExtra("room");
        info = intent.getStringExtra("info");
        Toast.makeText(getApplicationContext(), room+"입니다", Toast.LENGTH_SHORT).show();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_upload:
                Intent it = new Intent(Intent.ACTION_PICK);
                it.setType(MediaStore.Images.Media.CONTENT_TYPE);
                it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                it.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it, REQUEST_CODE);
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public String getEmail(){
        return id;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            ClipData clipData = data.getClipData();
            Uri uri = data.getData();
            if(clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    uris.add(clipData.getItemAt(i).getUri());
                }
            }else if(uri != null)
                uris.add(uri);


            /*
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uris.get(0), filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            cursor.close();
            */

            InputStream in;
            try {
                in = getContentResolver().openInputStream(uri);
                chatFragment tf = (chatFragment) getSupportFragmentManager().findFragmentById(R.id.chat_content);
                tf.sendStr("sj", getExifInfo(in));
                // Now you can extract any Exif tag you want
                // Assuming the image is a JPEG or supported raw format
            } catch (IOException e) {
                // Handle any errors
            }
        }
    }

    public String getExifInfo(InputStream filepath)
    {
        ExifInterface exif = null;
        String attr = "";
        try
        {
            exif = new ExifInterface(filepath);
        }
        catch (IOException e)
        {
            Log.e("TAG", "cannot read exif");
            e.printStackTrace();
        }
        if (exif != null)
        {
            attr += String.valueOf(exif.getAttribute(ExifInterface.TAG_DATETIME));
            attr += String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            attr += String.valueOf(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));

        }
        return attr;
    }
}
