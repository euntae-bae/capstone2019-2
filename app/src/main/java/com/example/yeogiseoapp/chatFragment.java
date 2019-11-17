package com.example.yeogiseoapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class chatFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    chatAdapter mAdapter;
    String name;
    List<chat> l = new ArrayList<>();
    Button sendBtn;
    EditText editText;

    private String TAG = "ChatFragment";
    private Socket mSocket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        name = ((roomActivity)getActivity()).getName();
        sendBtn = (Button)v.findViewById(R.id.sendbtn);
        editText = (EditText)v.findViewById(R.id.message);
        try {
            mSocket = IO.socket("http://ec2-54-180-107-241.ap-northeast-2.compute.amazonaws.com:8806");
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocket.emit("message_from_client", "Server Connected");
            }
        }).on("message_from_server", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        sendStr(name, args[0].toString());
                    }
                });
            }
        });
        mSocket.connect();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                mSocket.emit("message_from_client", msg);
            }
        });
        return v;
    }

    public void initChat() {
        recyclerView = (RecyclerView)getView().findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        name = ((roomActivity)getActivity()).getName();

        // specify an adapter (see also next example)
        mAdapter = new chatAdapter(l, name);
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyItemInserted(l.size() -1);
    }

    public void sendStr(String name, String txt)
    {
        chat c = new chat(name, txt);
        l.add(c);
        mAdapter.notifyItemInserted(l.size() -1);
        recyclerView.scrollToPosition(l.size()-1);
        editText.setText("");
    }
}
