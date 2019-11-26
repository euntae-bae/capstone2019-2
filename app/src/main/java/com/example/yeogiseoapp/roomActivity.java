package com.example.yeogiseoapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;

public class roomActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{
    private AppBarConfiguration mAppBarConfiguration;
    String id, room, info, name;
    private static final int REQUEST_CODE = 200;
    ArrayList<PhotoInfo> photoInfoList = new ArrayList<PhotoInfo>();
    chatFragment cf;
    mapFragment mf;
    ArrayList<Bitmap> smallPics = new ArrayList<Bitmap>();
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("nickname");
        cf = (chatFragment) getSupportFragmentManager().findFragmentById(R.id.chat_content);
        mf = (mapFragment) getSupportFragmentManager().findFragmentById(R.id.map_content);

        cf.initChat();
        room = intent.getStringExtra("room");
        info = intent.getStringExtra("info");
        Toast.makeText(getApplicationContext(), room+"입니다", Toast.LENGTH_SHORT).show();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

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
        navigationView.setNavigationItemSelectedListener(this);


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
    public String getName(){
        return name;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE) {

            ClipData clipData = data.getClipData();
            Uri uri = data.getData();
            PhotoInfo temp = new PhotoInfo();
            if(clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    photoInfoList.add(new PhotoInfo(clipData.getItemAt(i).getUri()));
                }
            }else if(uri != null) {
                photoInfoList.add(new PhotoInfo(uri));
            }

            InputStream in = null;
            String imgpath = "";
            try {
                for(int i=0; i<photoInfoList.size(); i++){
                    in = getContentResolver().openInputStream(photoInfoList.get(i).uri);
                    temp = getExifInfo(in);
                    photoInfoList.get(i).time = temp.time;
                    photoInfoList.get(i).longitude = temp.longitude;
                    photoInfoList.get(i).latitude = temp.latitude;
                    if(photoInfoList.get(i).latitude == -1 || photoInfoList.get(i).longitude == -1){
                        photoInfoList.remove(photoInfoList.get(i));
                        continue;
                    }
                }
                photoInfoList.sort(new Comparator<PhotoInfo>() {
                    @Override
                    public int compare(PhotoInfo arg0, PhotoInfo arg1) {
                        // TODO Auto-generated method stub
                        long t0 = arg0.time;
                        long t1 = arg1.time;
                        if (t0 == t1)
                            return 0;
                        else if (t0 > t1)
                            return 1;
                        else
                            return -1;
                    }
                });

                Bitmap src;
                Drawable drawable;
                for(int i=0; i<photoInfoList.size(); i++){
                    cf.sendStr(name, String.valueOf(photoInfoList.get(i).time) + '\n'
                            + String.valueOf(photoInfoList.get(i).latitude) + '\n'
                            + String.valueOf(photoInfoList.get(i).longitude));

                    src = decodeSampledBitmapFromUri(this, photoInfoList.get(i).uri, 150, 75);
                    smallPics.add(src);

                    drawable = new BitmapDrawable(this.getResources(), src);
                    navigationView.getMenu().add(Menu.NONE, Menu.FIRST, Menu.NONE, "Picture"+(i+1)).setIcon(drawable);

                    if(photoInfoList.get(i).longitude != -1 && photoInfoList.get(i).latitude != -1)
                        mf.makeMarker(photoInfoList.get(i).latitude, photoInfoList.get(i).longitude, src);
                }
                mf.drawPath();
            } catch (IOException e) {
                // Handle any errors
            }
        }
    }

    public PhotoInfo getExifInfo(InputStream filepath)
    {
        ExifInterface exif = null;
        Uri u = null;
        PhotoInfo attr = new PhotoInfo();
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
            attr.setInfo(u, exif.getAttribute(ExifInterface.TAG_DATETIME),
                         exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE),
                         exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
        }
        return attr;
    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri imageUri, int reqWidth, int reqHeight) throws FileNotFoundException {
        Bitmap bitmap = null;
        try {
            // Get input stream of the image
            final BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream iStream = context.getContentResolver().openInputStream(imageUri);

            // First decode with inJustDecodeBounds=true to check dimensions
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(iStream, null, options);
            if (iStream != null) {
                iStream.close();
            }
            iStream = context.getContentResolver().openInputStream(imageUri);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(iStream, null, options);
            if (iStream != null) {
                iStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String id = String.valueOf(item.getTitle());
        String temp = "";
        int n = 0;

        for(int i=7; i<id.length(); i++){
            temp += id.charAt(i);
        }

        n = Integer.parseInt(temp);
        cf.sendStr("mmola", temp);
        mf.moveCamera(mf.markers.get(n-1));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
