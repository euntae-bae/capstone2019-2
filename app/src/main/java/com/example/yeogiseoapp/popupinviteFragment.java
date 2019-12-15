package com.example.yeogiseoapp;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

// 그룹에 유저 초대 창을 띄워주는 Fragment이다.
public class popupinviteFragment extends DialogFragment {
    EditText inviteEdtText;
    public static popupinviteFragment newInstance() {
        Bundle bundle = new Bundle();
        popupinviteFragment fragment = new popupinviteFragment();
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.popup_invite, null);
        builder.setView(view);
        inviteEdtText = view.findViewById(R.id.inviteEdtText);
        return builder.create();
    }

    public void dismissDialog() {
        this.dismiss();
    }

    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public String getInviteEmail(){ return inviteEdtText.getText().toString(); }
}