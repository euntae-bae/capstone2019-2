package com.example.yeogiseoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.MapView;

public class mapOverlay extends Fragment {
    Button back_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mapoverlay, container, false);

        back_btn = v.findViewById(R.id.exitPaintBtn);
        back_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((roomActivity)getActivity()).deleteFrag(v);
            }
        });

        return v;
    }
}

class Paper extends View {

    Paint paint = new Paint();
    Path path = new Path();

    float y ;
    float x ;

    public Paper(Context context) {
        super(context);
    }

    public Paper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        if(((roomActivity)getContext()).isDrawing && ((roomActivity)getContext()).chkDrawstatus == 2) {
            ((roomActivity) getContext()).cfPathEmit(path);
            canvas.drawPath(path, paint);
        }else if(((roomActivity)getContext()).isDrawing && ((roomActivity)getContext()).chkDrawstatus == 1) {
            ((roomActivity) getContext()).cfPathEmit(path);
            canvas.drawPath(path, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        x = event.getX();
        y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        invalidate();

        return true;
    }

}