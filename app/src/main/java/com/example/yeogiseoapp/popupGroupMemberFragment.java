package com.example.yeogiseoapp;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class popupGroupMemberFragment extends DialogFragment {
    ListView groupMemberListview;
    GroupMemberAdapter adapter;
    ArrayList<String> groupNameList;
    ArrayList<String> groupEmailList;
    public static popupGroupMemberFragment newInstance() {
        Bundle bundle = new Bundle();
        popupGroupMemberFragment fragment = new popupGroupMemberFragment();
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.popup_groupmember, null);
        builder.setView(view);
        groupMemberListview = view.findViewById(R.id.groupMemberListView);
        adapter = new GroupMemberAdapter();
        groupMemberListview.setAdapter(adapter);

        for(int i=0; i<groupEmailList.size(); i++)
            makeMember(groupNameList.get(i), groupEmailList.get(i));

        return builder.create();
    }

    public void dismissDialog() {
        this.dismiss();
    }

    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public void setList(ArrayList<String> nameList, ArrayList<String> emailList){
        groupEmailList = emailList;
        groupNameList = nameList;
    }

    public void makeMember(String name, String email){
        adapter.addItem(name, email);
        adapter.notifyDataSetChanged();
    }
}