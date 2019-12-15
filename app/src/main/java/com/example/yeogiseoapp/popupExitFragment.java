package com.example.yeogiseoapp;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

// 그룹 나가기 창을 띄워주는 Fragment이다.
public class popupExitFragment extends DialogFragment {
    public static popupExitFragment newInstance() {
        Bundle bundle = new Bundle();
        popupExitFragment fragment = new popupExitFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.popup_exit, null);
        builder.setView(view);
        return builder.create();
    }

    public void dismissDialog() {
        this.dismiss();
    }

    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}