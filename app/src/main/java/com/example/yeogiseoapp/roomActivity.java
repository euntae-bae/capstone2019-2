package com.example.yeogiseoapp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeogiseoapp.data.CommentData;
import com.example.yeogiseoapp.data.CommentResponse;
import com.example.yeogiseoapp.data.ExifData;
import com.example.yeogiseoapp.data.ExifUploadResponse;
import com.example.yeogiseoapp.data.ExitGroupData;
import com.example.yeogiseoapp.data.ExitGroupResponse;
import com.example.yeogiseoapp.data.FindUserData;
import com.example.yeogiseoapp.data.FindUserResponse;
import com.example.yeogiseoapp.data.GetPhotoInfoData;
import com.example.yeogiseoapp.data.GetPhotoInfoResponse;
import com.example.yeogiseoapp.data.GroupMemberListData;
import com.example.yeogiseoapp.data.GroupMemberListResponse;
import com.example.yeogiseoapp.data.ImageUploadResponse;
import com.example.yeogiseoapp.data.InviteData;
import com.example.yeogiseoapp.data.InviteResponse;
import com.example.yeogiseoapp.data.RemoveGroupData;
import com.example.yeogiseoapp.data.RemoveGroupResponse;
import com.example.yeogiseoapp.data.RemoveImageData;
import com.example.yeogiseoapp.data.RemoveImageResponse;
import com.example.yeogiseoapp.data.ScheduleData;
import com.example.yeogiseoapp.data.ScheduleResponse;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class roomActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{
    private AppBarConfiguration mAppBarConfiguration;
    private ServiceApi service = null;

    String email, info, username, room;
    private static final int REQUEST_CODE = 200;
    ArrayList<PhotoInfo> photoInfoList = new ArrayList<>();
    ArrayList<ExifData> exifDataArrayList = new ArrayList<>();
    ArrayList<ScheduleInfo> scheduleInfoArrayList = new ArrayList<>();
    chatFragment cf;
    mapFragment mf;
    popupinviteFragment inviteFragment;
    popuptogetherFragment pf;
    popupExitFragment exitFragment;
    popupGroupMemberFragment groupMemberFragment;
    NavigationView navigationView;
    boolean isDrawing;
    // 0 : No, 1 : Yes, 2 : Host
    int chkDrawstatus, gid, id, tempUid;
    public Paper paper;


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
        gid = intent.getIntExtra("gid", 0);
        id = intent.getIntExtra("id", 0);

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
        return email;
    }
    public String getUsername(){
        return username;
    }
    public String getRoom() { return room; }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ArrayList<PhotoInfo> checkedImgList = new ArrayList<>();
        navigationView.getMenu().clear();
        if (requestCode == REQUEST_CODE) {

            List<MultipartBody.Part> parts = new ArrayList<>();

            ClipData clipData;
            if(data.getClipData() == null && data.getData() == null)
                return ;
            else
                clipData = data.getClipData();
            Uri uri = data.getData();
            PhotoInfo temp = new PhotoInfo();
            if(clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    checkedImgList.add(new PhotoInfo(clipData.getItemAt(i).getUri()));
                }
            }else if(uri != null) {
                checkedImgList.add(new PhotoInfo(uri));
            }
            InputStream in = null;
            String imgpath = "";
            try {
                for(int i=0; i<checkedImgList.size(); i++){
                    in = getContentResolver().openInputStream(checkedImgList.get(i).uri);
                    temp = getExifInfo(in);

                    String filepath = checkedImgList.get(i).getRealPathFromURI(this);
                    String filename = filepath.substring(filepath.lastIndexOf("/")+1);
                    checkedImgList.get(i).time = temp.time;
                    checkedImgList.get(i).longitude = temp.longitude;
                    checkedImgList.get(i).latitude = temp.latitude;
                    checkedImgList.get(i).orientation = temp.orientation;
                    if(checkedImgList.get(i).latitude == -1 || checkedImgList.get(i).longitude == -1){
                        checkedImgList.remove(checkedImgList.get(i));
                    }
                }
                checkedImgList.sort(new Comparator<PhotoInfo>() {
                    @Override
                    public int compare(PhotoInfo arg0, PhotoInfo arg1) {
                        long t0 = arg0.time;
                        long t1 = arg1.time;
                        return Long.compare(t0, t1);
                    }
                });

                for(int i=0; i<checkedImgList.size(); i++){

                    //나중에 filename저장했다가 읽어오기만 하는걸로 변경 필요
                    String filepath = checkedImgList.get(i).getRealPathFromURI(this);
                    String filename = filepath.substring(filepath.lastIndexOf("/")+1);

                    //데이터 서버측으로 전송할 준비
                    // 데이터를 모아서 하나의 JSON으로 보내야 함.
                    //filename 읽어서 바꿔줘야 함

                    //TODO  보내는 정보에 내 클라이언트 정보(그룹ID, 유저ID 추가해서 보내야 함. 라우터측 작업도 필요)
                    exifDataArrayList.add(new ExifData(filename,checkedImgList.get(i).longitude,checkedImgList.get(i).latitude,checkedImgList.get(i).time,id,gid));
                    parts.add(prepareFilePart("photo",filepath));
                }



                //EXIF 업로드
                service.exifUpload(exifDataArrayList).enqueue(new Callback<List<ExifUploadResponse>>() {
                    @Override
                    public void onResponse(Call<List<ExifUploadResponse>> call, Response<List<ExifUploadResponse>> response) {
                        ExifUploadResponse result = response.body().get(0);
                        int code = result.getCode();
                        String message = result.getMessage();
                        if(code == 404){
                            // Toast.makeText(roomActivity.this,"Error",Toast.LENGTH_SHORT).show();
                        }else{
                            //Toast.makeText(roomActivity.this,message,Toast.LENGTH_SHORT).show();
                            //업로드쪽 서비스 시작.
                            //showToast(result.getimgID(0));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ExifUploadResponse>> call, Throwable t) {
                        Toast.makeText(roomActivity.this,"전송 오류 발생",Toast.LENGTH_SHORT).show();
                        Log.e("전송 오류 발생", t.getMessage());
                    }
                });




                // 파일 실제 전송하는 부분
                RequestBody description = createPart(gid);

                service.imageUploadDynamic(description,parts).enqueue(new Callback<ImageUploadResponse> (){
                    @Override
                    public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                        Toast.makeText(roomActivity.this,"업로드 성공",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                        Toast.makeText(roomActivity.this,"업로드 오류 발생",Toast.LENGTH_SHORT).show();
                        Log.e("이미지 전송 오류 발생", t.getMessage());
                    }
                });


                // 맵에 마커 찍는 부분
                initSchedule(new ScheduleData(gid));

            } catch (IOException e) {
                // Handle any errors
            }

            //보낸 데이터 정리해보리기
            exifDataArrayList.clear();
            checkedImgList.clear();
        }
    }

    public void makeMarkerByPhotoList(boolean isUri){
        Drawable drawable;
        Bitmap pic;
        for(int i=0; i<photoInfoList.size(); i++){
            photoInfoList.get(i).id = i;

            drawable = getDrawable(R.drawable.ic_menu_camera);
            navigationView.getMenu().add(Menu.NONE, Menu.FIRST, Menu.NONE, "Picture"+(i+1)).setIcon(drawable);

            if(isUri)
                pic = photoInfoList.get(i).getRotatedBitmap(this, 130, 87);
            else
                pic = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_camera);
            if(photoInfoList.get(i).longitude != -1 && photoInfoList.get(i).latitude != -1)
                mf.makeMarker(photoInfoList.get(i).latitude, photoInfoList.get(i).longitude, pic);
        }
        mf.drawPath();
    }

    public void makeNavigationMenuUsingPhotos(){
        for(int i=0; i<photoInfoList.size(); i++){
            Drawable drawable = getDrawable(R.drawable.ic_menu_camera);
            navigationView.getMenu().add(Menu.NONE, photoInfoList.get(i).id, Menu.NONE, "Picture"+(i+1)).setIcon(drawable);
        }
    }

    public void makeMarkerByScheduleList(boolean isUri){
        Bitmap pic;
        for(int i=0; i<scheduleInfoArrayList.size(); i++){
            if(isUri)
                pic = photoInfoList.get(scheduleInfoArrayList.get(i).imageList.get(0)-1).getRotatedBitmap(this, 130, 87);
            else
                pic = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_camera);
            if(scheduleInfoArrayList.get(i).longitude != -1 && scheduleInfoArrayList.get(i).latitude != -1)
                mf.makeMarker(scheduleInfoArrayList.get(i).latitude, scheduleInfoArrayList.get(i).longitude, pic);
        }
        mf.drawPath();
    }

    private RequestBody createPart(int descString){
        return RequestBody.create(MultipartBody.FORM, String.valueOf(descString));
    }

    private MultipartBody.Part prepareFilePart(String partName, String filepath){

        File imgFile = new File(filepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),imgFile);

        return MultipartBody.Part.createFormData(partName,imgFile.getName(),requestFile);

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





    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //mf.moveCamera(mf.markers.get(n-1));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView picImgView = (ImageView)findViewById(R.id.picBodyImage);
        TextView picTimeTextView = (TextView) findViewById(R.id.pictureTime);
        TextView picLatitudeTextView = (TextView) findViewById(R.id.pictureLatitude);
        TextView picLongitudeTextView = (TextView) findViewById(R.id.pictureLongitude);
        TextView picCommentTextView = (TextView) findViewById(R.id.pictureComment);
        TextView picIdTextView = (TextView) findViewById(R.id.pictureId);

        int n = 0;
        for(int i=0; i<photoInfoList.size(); i++){
            if(photoInfoList.get(i).id == id) {
                n = i;
                break;
            }
        }
        picImgView.setImageBitmap(photoInfoList.get(n).getRotatedBitmap(this, dpToPx(this, 100), dpToPx(this, 80)));
        picIdTextView.setText("ID : "+photoInfoList.get(n).id);
        picTimeTextView.setText("Time : "+String.valueOf(photoInfoList.get(n).time));
        picLatitudeTextView.setText("Latitude : "+String.valueOf(photoInfoList.get(n).latitude));
        picLongitudeTextView.setText("Longitude : "+String.valueOf(photoInfoList.get(n).longitude));
        picCommentTextView.setText("Comment : "+photoInfoList.get(n).comment);

        return true;
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

    public void mPicinfoOnClick(View v) {
        switch (v.getId()) {
            case R.id.picinfoSaveBtn:
                break;

            case R.id.picinfoDeleteBtn:
                int picId = Integer.parseInt(((TextView)findViewById(R.id.pictureId)).getText().toString().substring(5));
                removeImage(new RemoveImageData(picId));
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
    public int getGid() { return gid; }

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

    public void initSchedule(final ScheduleData data) {
        service.getSchedule(data).enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                ScheduleResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();
                int count = result.getCount();
                if (code == 201) {
                    // 스케쥴 생성 성공
                    scheduleInfoArrayList.clear();
                    for(int i=0; i<count; i++){
                        ScheduleInfo scheduleInfo = new ScheduleInfo();
                        scheduleInfo.dateAndTime = result.getListIndexTimeString(i);
                        scheduleInfo.latitude = result.getListIndexLatitude(i);
                        scheduleInfo.longitude = result.getListIndexLongitude(i);
                        scheduleInfo.scheduleID = result.getListIndexScheduleId(i);
                        scheduleInfo.imageList = result.getImgList(i);
                        scheduleInfoArrayList.add(scheduleInfo);
                    }
                    getPhotoInfoList(new GetPhotoInfoData(gid));
                }
            }
            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                Toast.makeText(roomActivity.this, "일정 초기화 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("일정 초기화 과정 오류 발생", t.getMessage());
            }
        });
    }

    public void getPhotoInfoList(final GetPhotoInfoData data) {
        service.getPhotoInfo(data).enqueue(new Callback<GetPhotoInfoResponse>() {
            @Override
            public void onResponse(Call<GetPhotoInfoResponse> call, Response<GetPhotoInfoResponse> response) {
                GetPhotoInfoResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();
                if (code == 201) {
                    photoInfoList.clear();
                    for(int i=0; i<result.getSize(); i++) {
                        PhotoInfo p = new PhotoInfo();
                        p.id = result.getImgId(i);
                        p.server_pathname = result.getImgPathname(i);
                        p.longitude = result.getImgLongitude(i);
                        p.latitude = result.getImgLatitude(i);
                        p.setTime(result.getImgdateAndTime(i));
                        p.groupID = result.getImgGroupID(i);
                        p.userID = result.getImgUserID(i);
                        p.order_in_group = i;
                        photoInfoList.add(p);
                    }
                    makeMarkerByScheduleList(false);
                    navigationView.getMenu().clear();
                    makeNavigationMenuUsingPhotos();
                }
            }
            @Override
            public void onFailure(Call<GetPhotoInfoResponse> call, Throwable t) {
                Toast.makeText(roomActivity.this, "이미지 받아오기 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("이미지 받아오기 과정 오류 발생", t.getMessage());
            }
        });
    }

    public void removeImage(final RemoveImageData data) {
        service.removeImage(data).enqueue(new Callback<RemoveImageResponse>() {
            @Override
            public void onResponse(Call<RemoveImageResponse> call, Response<RemoveImageResponse> response) {
                RemoveImageResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();
                if (code == 201) {
                    showToast(message);
                    initSchedule(new ScheduleData(gid));
                }
            }
            @Override
            public void onFailure(Call<RemoveImageResponse> call, Throwable t) {
                Toast.makeText(roomActivity.this, "이미지 삭제 과정 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("이미지 삭제 과정 오류 발생", t.getMessage());
            }
        });
    }

    public void setComment(final CommentData data) {
        service.comment(data).enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                CommentResponse result = response.body(); // 서버에서 보낸 응답의 역직렬화된 데이터
                int code = result.getCode();
                String message = result.getMessage();
                if (code == 201) {
                    showToast(message);
                }
            }
            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Toast.makeText(roomActivity.this, "코멘트 업로드 과정 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("코멘트 업로드 과정 오류 발생", t.getMessage());
            }
        });
    }


    public void setTempUid(int uid){ tempUid = uid; }

    public void showToast(String s){
        Toast.makeText(roomActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    public void goHall(){
        Intent intent = new Intent(this, HallActivity.class);
        startActivity(intent);
    }

    public int dpToPx(Context context, float dp){
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }
    public float pxToDp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        if(density == 1.0)
            density *= 4.0;
        else if(density == 1.5)
            density *= (8/3);
        else if(density == 2.0)
            density *= 2.0;

        return px/density;
    }


    private class ImageDownload extends AsyncTask<String, Void, Void> {

        /**
         * 파일명
         */
        private String fileName;

        /**
         * 저장할 폴더
         */
        private final String SAVE_FOLDER = "/yeogiseo/save_folder";

        @Override
        protected Void doInBackground(String... params) {

            //다운로드 경로를 지정
            String savePath = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;

            File dir = new File(savePath);

            //상위 디렉토리가 존재하지 않을 경우 생성
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //파일 이름 :날짜_시간
            Date day = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);
            fileName = String.valueOf(sdf.format(day));

            //웹 서버 쪽 파일이 있는 경로
            String fileUrl = params[0];

            //다운로드 폴더에 동일한 파일명이 존재하는지 확인
            if (new File(savePath + "/" + fileName).exists() == false) {
            } else {
            }

            String localPath = savePath + "/" + fileName + ".jpg";

            try {
                URL imgUrl = new URL(fileUrl);
                //서버와 접속하는 클라이언트 객체 생성
                HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();
                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];
                //입력 스트림을 구한다
                InputStream is = conn.getInputStream();
                File file = new File(localPath);
                //파일 저장 스트림 생성
                FileOutputStream fos = new FileOutputStream(file);
                int read;
                //입력 스트림을 파일로 저장
                for (;;) {
                    read = is.read(tmpByte);
                    if (read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, read); //file 생성
                }
                is.close();
                fos.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //저장한 이미지 열기
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String targetDir = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;
            File file = new File(targetDir + "/" + fileName + ".jpg");
            //type 지정 (이미지)
            i.setDataAndType(Uri.fromFile(file), "image/*");
            getApplicationContext().startActivity(i);
            //이미지 스캔해서 갤러리 업데이트
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        }
    }

}
