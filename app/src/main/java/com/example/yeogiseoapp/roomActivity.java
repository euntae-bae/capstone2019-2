package com.example.yeogiseoapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.yeogiseoapp.data.ExifData;
import com.example.yeogiseoapp.data.ExifUploadResponse;
import com.example.yeogiseoapp.data.ImageUploadResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class roomActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{
    private AppBarConfiguration mAppBarConfiguration;
    private ServiceApi service = null;

    String id, info, name, room;
    private static final int REQUEST_CODE = 200;
    ArrayList<PhotoInfo> photoInfoList = new ArrayList<PhotoInfo>();
    ArrayList<ExifData> exifDataArrayList = new ArrayList<>();
    chatFragment cf;
    mapFragment mf;
    mapOverlay mo;
    ArrayList<Bitmap> smallPics = new ArrayList<Bitmap>();
    NavigationView navigationView;
    boolean isDrawing;
    // 0 : No, 1 : Yes, 2 : Host
    int chkDrawstatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        isDrawing = false;
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("nickname");
        cf = (chatFragment) getSupportFragmentManager().findFragmentById(R.id.chat_content);
        mf = (mapFragment) getSupportFragmentManager().findFragmentById(R.id.map_content);
        mo = (mapOverlay) getSupportFragmentManager().findFragmentById(R.id.canvasfrag);
        room = intent.getStringExtra("room");
        info = intent.getStringExtra("info");

        //전송
        service = RetrofitClient.getInstance().create(ServiceApi.class);

        cf.initChat();

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

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // use the FileUtils to get the actual file by uri
        File file = new File(fileUri.getPath());

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public String getEmail(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getRoom() { return room; }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE) {

            //클립보드에서 데이터 가져옴
            ClipData clipData = data.getClipData();
            Uri uri = data.getData();
            PhotoInfo temp = new PhotoInfo();
            if(clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    photoInfoList.add(new PhotoInfo(clipData.getItemAt(i).getUri()));
                    //여러개라 클립보드에서 가져왔을 때
                }
            }else if(uri != null) {
                photoInfoList.add(new PhotoInfo(uri));
                //하나일 때
            }

            InputStream in = null;
            String imgpath = "";
            try {

                //TODO :: 업로드 시에 여러번에 걸쳐서 업로드하면 오류가 생기는 듯 함
                //이전 이미지와 같이 스택이 되어서 보내짐(메타데이터가, DB에)


                //사진을 시간순대로 정렬(소트)
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

                //데이터 작업쳐서 가져옴
                for(int i=0; i<photoInfoList.size(); i++){
                    in = getContentResolver().openInputStream(photoInfoList.get(i).uri);
                    temp = getExifInfo(in);

                    String filepath = getRealPathFromURI(photoInfoList.get(i).uri,this);
                    String filename = filepath.substring(filepath.lastIndexOf("/")+1);
                    photoInfoList.get(i).time = temp.time;
                    photoInfoList.get(i).longitude = temp.longitude;
                    photoInfoList.get(i).latitude = temp.latitude;
                    photoInfoList.get(i).orientation = temp.orientation;
                    if(photoInfoList.get(i).latitude == -1 || photoInfoList.get(i).longitude == -1){
                        photoInfoList.remove(photoInfoList.get(i));
                        continue;
                    }

                    //데이터 서버측으로 전송할 준비
                    // 데이터를 모아서 하나의 JSON으로 보내야 함.
                    //filename 읽어서 바꿔줘야 함
                    exifDataArrayList.add(new ExifData(filename,temp.longitude,temp.latitude,temp.time));

                }

                service.exifUpload(exifDataArrayList).enqueue(new Callback<ExifUploadResponse>() {
                    @Override
                    public void onResponse(Call<ExifUploadResponse> call, Response<ExifUploadResponse> response) {
                        ExifUploadResponse result = response.body();
                        int code = result.getCode();
                        String message = result.getMessage();

                        if(code == 404){
                            Toast.makeText(roomActivity.this,"Error",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(roomActivity.this,message,Toast.LENGTH_SHORT).show();
                            //업로드쪽 서비스 시작.
                        }

                    }

                    @Override
                    public void onFailure(Call<ExifUploadResponse> call, Throwable t) {
                        Toast.makeText(roomActivity.this,"전송 오류 발생",Toast.LENGTH_SHORT).show();
                        Log.e("전송 오류 발생", t.getMessage());
                    }
                });




                //TODO : 파일 실제 전송(multer)

                List<MultipartBody.Part> parts = new ArrayList<>();
                //part의 리스트

                parts.add(prepareFilePart("photo",photoInfoList.get(0).uri));

                RequestBody description = RequestBody.create(
                        okhttp3.MultipartBody.FORM, "photo");
                //desc생성

                service.imageUploadDynamic(parts,description).enqueue(new Callback<ImageUploadResponse>() {
                    @Override
                    public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                        int code = response.body().getCode();
                        if(code == 500) {
                            Toast.makeText(roomActivity.this,"업로드 서버측 오류 발생",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(roomActivity.this,"업로드 성공",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                        Toast.makeText(roomActivity.this,"업로드 오류 발생",Toast.LENGTH_SHORT).show();
                        Log.e("업로드 오류 발생", t.getMessage());
                    }
                });


                //보낸 데이터 정리해보리기
                exifDataArrayList.clear();

                // 맵에 마커 찍는 부분
                Bitmap src;
                Drawable drawable;
                navigationView.getMenu().clear();
                for(int i=0; i<photoInfoList.size(); i++){
                    cf.sendStr(name, String.valueOf(photoInfoList.get(i).time) + '\n'
                            + String.valueOf(photoInfoList.get(i).latitude) + '\n'
                            + String.valueOf(photoInfoList.get(i).longitude));

                    src = rotateBitmap(decodeSampledBitmapFromUri(this, photoInfoList.get(i).uri, 120, 60), photoInfoList.get(i).orientation);

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
                         exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE),
                         exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED));
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
        mf.moveCamera(mf.markers.get(n-1));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public void openTogetherPopup(String who){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, popuptogetherActivity.class);
        intent.putExtra("data", who);
        startActivityForResult(intent, 1);
    }

    public void sendAllow(){ cf.emitTogether(); }
    public void cfPathEmit(Path path) { cf.emitPath(path); }
    public void openOverlay(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.add(R.id.canvasfrag,new mapOverlay());
        ft.addToBackStack(null);
        chkDrawstatus = 2;
        isDrawing = true;
        ft.commit();
    }

    public void deleteFrag(View v){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }
        isDrawing = false;
        ft.commit();
    }
}
