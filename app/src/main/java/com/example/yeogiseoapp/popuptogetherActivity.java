package com.example.yeogiseoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class popuptogetherActivity extends Activity {

    TextView txtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_together);

        //UI 객체생성
        txtText = (TextView)findViewById(R.id.txtText);

        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        txtText.setText(data + "님의 같이보기 요청");
    }

    //거절 버튼 클릭
    public void mOnAllow(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "togetheryes");
        setResult(RESULT_OK, intent);

        Toast.makeText(this, "YES", Toast.LENGTH_SHORT).show();
        //액티비티(팝업) 닫기
        finish();
    }

    //거절 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "togetherno");
        setResult(RESULT_OK, intent);

        Toast.makeText(this, "NO", Toast.LENGTH_SHORT).show();

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}