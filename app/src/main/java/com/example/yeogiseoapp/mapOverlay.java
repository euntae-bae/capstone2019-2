package com.example.yeogiseoapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

// 구글맵 위에 그려지게 하는 Fragment이다.
public class mapOverlay extends Fragment {
    Button back_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mapoverlay, container, false);
        ((roomActivity)getActivity()).paper = v.findViewById(R.id.paper);
        back_btn = v.findViewById(R.id.exitPaintBtn);
        back_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((roomActivity)getActivity()).setChkDrawstatus(0);
                ((roomActivity)getActivity()).setisDrawing(false);
                ((roomActivity)getActivity()).deleteFrag(v);
            }
        });
        return v;
    }
}

class Paper extends View {
    Handler timerHandler;

    final static int repeat_delay = 50;
    public String test= "test";

    Paint paint = new Paint();
    Path path = new Path();

    float y ;
    float x ;

    boolean amIdrawing = false;
    boolean chkDrawing = false;

    public Paper(Context context) {
        super(context);
        init();
    }

    public Paper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void onDraw(Canvas canvas) {
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(amIdrawing) {
            x = event.getX();
            y = event.getY();

            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    chkDrawing = true;
                    path.moveTo(x, y);
                    timerHandler.sendEmptyMessage(0);
                    break;
                case MotionEvent.ACTION_MOVE:
                    x = event.getX();
                    y = event.getY();
                    path.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    timerHandler.removeMessages(0);
                    ((roomActivity)getContext()).emitStopDrawing();
                    chkDrawing = false;
                    break;
            }

            invalidate();

            return true;
        }
        return true;
    }

    public void receivePath(float ax, float ay){
        int px = dpToPx(getContext(), ax);
        int py = dpToPx(getContext(), ay);

        if(!chkDrawing){
            path.moveTo(px, py);
            chkDrawing = true;
        }else{
            path.lineTo(px, py);
        }

        invalidate();
    }

    private void init() {
        timerHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                ((roomActivity)getContext()).cfPathEmit(pxToDp(getContext(), x), pxToDp(getContext(), y));
                this.sendEmptyMessageDelayed(0, repeat_delay);
            }
        };

        amIdrawing = (((roomActivity)getContext()).getChkDrawstatus() == 2) ? true : false;
    }

    public void setIamDrawing(boolean b){ amIdrawing = b; }
    public void setDrawing(boolean b){ chkDrawing = b; }

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
}