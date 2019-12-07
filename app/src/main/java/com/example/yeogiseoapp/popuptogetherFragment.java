package com.example.yeogiseoapp;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

public class popuptogetherFragment extends DialogFragment {

    private static final String TAG = "CustomDialogFragment";    private static final String ARG_DIALOG_MAIN_MSG = "dialog_main_msg";
    private String mMainMsg;
    public static popuptogetherFragment newInstance(String mainMsg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_DIALOG_MAIN_MSG, mainMsg);
        popuptogetherFragment fragment = new popuptogetherFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMainMsg = getArguments().getString(ARG_DIALOG_MAIN_MSG);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.popup_together, null);
        ((TextView)view.findViewById(R.id.popBodyText)).setText(mMainMsg);
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