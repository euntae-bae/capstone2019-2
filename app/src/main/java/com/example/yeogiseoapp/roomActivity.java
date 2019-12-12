package com.example.yeogiseoapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.yeogiseoapp.data.ExitGroupData;
import com.example.yeogiseoapp.data.ExitGroupResponse;
import com.example.yeogiseoapp.data.FindUserData;
import com.example.yeogiseoapp.data.FindUserResponse;
import com.example.yeogiseoapp.data.GroupData;
import com.example.yeogiseoapp.data.GroupMemberListData;
import com.example.yeogiseoapp.data.GroupMemberListResponse;
import com.example.yeogiseoapp.data.GroupResponse;
import com.example.yeogiseoapp.data.InviteData;
import com.example.yeogiseoapp.data.InviteResponse;
import com.example.yeogiseoapp.data.RemoveGroupData;
import com.example.yeogiseoapp.data.RemoveGroupResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class roomActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{
    private AppBarConfiguration mAppBarConfiguration;
    String email, info, username, room, gid, id;
    private static final int REQUEST_CODE = 200;
    ArrayList<PhotoInfo> photoInfoList = new ArrayList<PhotoInfo>();
    chatFragment cf;
    mapFragment mf;
    popupinviteFragment inviteFragment;
    popuptogetherFragment pf;
    popupExitFragment exitFragment;
    popupGroupMemberFragment groupMemberFragment;
    ArrayList<Bitmap> smallPics = new ArrayList<Bitmap>();
    NavigationView navigationView;
    boolean isDrawing;
    // 0 : No, 1 : Yes, 2 : Host
    int chkDrawstatus;
    public Paper paper;
    ServiceApi service = null;
    String tempUid = null;
    ArrayList<String> groupMemberArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        isDrawing = false;
        Intent intent = getIntent();
        service = RetrofitClient.getInstance().create(ServiceApi.class);
        email = intent.getStringExtra("email");
        username = intent.getStringExtra("username");
        cf = (chatFragment) getSupportFragmentManager().findFragmentById(R.id.chat_content);
        mf = (mapFragment) getSupportFragmentManager().findFragmentById(R.id.map_content);
        room = intent.getStringExtra("room");
        info = intent.getStringExtra("info");
        gid = intent.getStringExtra("gid");
        id = intent.getStringExtra("id");

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
            case R.id.action_invite:
                openInvitePopup();
                return true;
            case R.id.action_upload:
                Intent it = new Intent(Intent.ACTION_PICK);
                it.setType(MediaStore.Images.Media.CONTENT_TYPE);
                it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                it.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it, REQUEST_CODE);
                return true;
            case R.id.action_list:
                groupMemberList(new GroupMemberListData(gid), true);
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_exit:
                openExitPopup();
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
        return email;
    }
    public String getUsername(){
        return username;
    }
    public String getRoom() { return room; }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE) {

            ClipData clipData;
            if(data.getClipData() == null)
                return ;
            else
                clipData = data.getClipData();
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
                    photoInfoList.get(i).orientation = temp.orientation;
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
                navigationView.getMenu().clear();
                for(int i=0; i<photoInfoList.size(); i++){
                    cf.sendStr(username, String.valueOf(photoInfoList.get(i).time) + '\n'
                            + String.valueOf(photoInfoList.get(i).latitude) + '\n'
                            + String.valueOf(photoInfoList.get(i).longitude));

                    src = rotateBitmap(decodeSampledBitmapFromUri(this, photoInfoList.get(i).uri, 130, 87), photoInfoList.get(i).orientation);

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
        popuptogetherFragment dialog = popuptogetherFragment.newInstance(
                getString(R.string.allow_popup_dialog_msg, who)
        );
        pf = dialog;
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void openInvitePopup(){
        popupinviteFragment dialog = popupinviteFragment.newInstance();
        inviteFragment = dialog;
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void openExitPopup(){
        popupExitFragment dialog = popupExitFragment.newInstance();
        exitFragment = dialog;
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void openGroupMemberPopup(ArrayList<String> ns, ArrayList<String> es){
        popupGroupMemberFragment dialog = popupGroupMemberFragment.newInstance();
        groupMemberFragment = dialog;
        groupMemberFragment.setList(ns, es);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void sendAllow(){ cf.emitTogether(); }
    public void cfPathEmit(float x, float y) { cf.emitPath(x, y); }
    public void openOverlay(int i){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.add(R.id.canvasfrag,new mapOverlay());
        ft.addToBackStack(null);
        chkDrawstatus = i;

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

    public void mAllowOnClick(View v) {
        switch (v.getId()) {
            case R.id.popAllowBtn:
                openOverlay(1);
                isDrawing = true;
                pf.dismissDialog();
                break;

            case R.id.popDeclineBtn:
                isDrawing = false;
                pf.dismissDialog();
                break;
        }
    }

    public void mInviteOnClick(View v) {
        switch (v.getId()) {
            case R.id.popInviteOkBtn:
                String inviteEmail = inviteFragment.getInviteEmail();
                findUserid(new FindUserData(inviteEmail));
                break;

            case R.id.popInviteNoBtn:
                inviteFragment.dismissDialog();
                break;
        }
    }

    public void mExitOnClick(View v) {
        switch (v.getId()) {
            case R.id.popExitOkBtn:
                groupMemberList(new GroupMemberListData(gid), false);
                exitGroup(new ExitGroupData(gid, id));
                exitFragment.dismissDialog();
                goHall();
                break;

            case R.id.popExitNoBtn:
                exitFragment.dismissDialog();
                break;
        }
    }

    public void mGroupMemberOnClick(View v) {
        switch (v.getId()) {
            case R.id.popGroupMemberOkBtn:
                groupMemberFragment.dismissDialog();
                break;
        }
    }

    public void drawByReceivedData(float x, float y){
            paper.receivePath(x, y);
    }
    public void setChkDrawstatus(int i){ chkDrawstatus = i; }
    public void setisDrawing(boolean b){ isDrawing = b; }
    public int getChkDrawstatus(){ return chkDrawstatus; }


    public boolean getIsDrawing(){ return isDrawing; }
    public void emitStopDrawing(){ cf.emitStop(); }
    public void stopDrawing(){ paper.chkDrawing = false; }

    private void inviteUser(final InviteData data) {
        service.invite(data).enqueue(new Callback<InviteResponse>() {
            @Override
            public void onResponse(Call<InviteResponse> call, Response<InviteResponse> response) {
                InviteResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();

                if (code == 201) {
                    // 그룹 생성 성공
                    Toast.makeText(roomActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    // 그룹 생성 실패
                    Toast.makeText(roomActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InviteResponse> call, Throwable t) {
                Toast.makeText(roomActivity.this, "초대 과정 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("초대 과정 오류 발생", t.getMessage());
            }
        });
    }

    private void findUserid(final FindUserData data) {
        service.findUser(data).enqueue(new Callback<FindUserResponse>() {
            @Override
            public void onResponse(Call<FindUserResponse> call, Response<FindUserResponse> response) {
                FindUserResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();

                if (code == 201) {
                    // 유저 검색 성공
                    setTempUid(result.getId());
                    inviteUser(new InviteData(gid, tempUid));
                    inviteFragment.dismissDialog();
                }
                else {
                    // 유저 검색 실패
                    inviteFragment.dismissDialog();
                    return ;
                }
            }

            @Override
            public void onFailure(Call<FindUserResponse> call, Throwable t) {
                Toast.makeText(roomActivity.this, "유저 검색 과정 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("유저 검색 과정 오류 발생", t.getMessage());
            }
        });
    }

    private void exitGroup(final ExitGroupData data) {
        service.exitGroup(data).enqueue(new Callback<ExitGroupResponse>() {
            @Override
            public void onResponse(Call<ExitGroupResponse> call, Response<ExitGroupResponse> response) {
                ExitGroupResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();

                if (code == 201) {
                    // 그룹 생성 성공
                    Toast.makeText(roomActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    // 그룹 생성 실패
                    Toast.makeText(roomActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExitGroupResponse> call, Throwable t) {
                Toast.makeText(roomActivity.this, "그룹 나가기 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("그룹 나가기 과정 오류 발생", t.getMessage());
            }
        });
    }

    private void removeGroup(final RemoveGroupData data) {
        service.removeGroup(data).enqueue(new Callback<RemoveGroupResponse>() {
            @Override
            public void onResponse(Call<RemoveGroupResponse> call, Response<RemoveGroupResponse> response) {
                RemoveGroupResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();

                if (code == 201) {
                    // 그룹 생성 성공
                    Toast.makeText(roomActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    // 그룹 생성 실패
                    Toast.makeText(roomActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RemoveGroupResponse> call, Throwable t) {
                Toast.makeText(roomActivity.this, "그룹 나가기 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("그룹 나가기 과정 오류 발생", t.getMessage());
            }
        });
    }

    private void groupMemberList(final GroupMemberListData data, final boolean isOpenPopup) {
        service.groupMemberList(data).enqueue(new Callback<GroupMemberListResponse>() {
            @Override
            public void onResponse(Call<GroupMemberListResponse> call, Response<GroupMemberListResponse> response) {
                GroupMemberListResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();
                int count = result.getCount();
                if (code == 201) {
                    // 그룹 생성 성공
                    if(isOpenPopup)
                        openGroupMemberPopup(result.getNameList(), result.getEmailList());
                    else if(count == 1)
                        removeGroup(new RemoveGroupData(data.getGroupID()));
                }
                else {
                    // 그룹 생성 실패
                    Toast.makeText(roomActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupMemberListResponse> call, Throwable t) {
                Toast.makeText(roomActivity.this, "그룹 멤버 조회 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("그룹 멤버 조회 과정 오류 발생", t.getMessage());
            }
        });
    }

    public void setTempUid(String uid){ tempUid = uid; }

    public void showToast(String s){
        Toast.makeText(roomActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    public void goHall(){
        Intent intent = new Intent(this, HallActivity.class);
        startActivity(intent);
    }
}
