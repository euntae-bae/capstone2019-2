package com.example.yeogiseoapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

// 채팅 화면이 속한 프래그먼트를 선언하는 부분이다.
public class chatFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    chatAdapter mAdapter;
    String room, name;
    List<chat> l = new ArrayList<>();
    Button sendBtn;
    EditText editText;

    private String TAG = "ChatFragment";
    private Socket mSocket;


    /*

    뷰가 생성될 때 socket을 초기화 하고 해당 socket의 함수들을 정의해준다.
    채팅 메시지를 주고 받을 때, 같이 보기 요청을 주고 받을 때, 그리기 정보를 주고 받을 때, 그리기 멈춤 신호를 주고 받을 때 등에 쓰인다.

     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        sendBtn = (Button)v.findViewById(R.id.sendbtn);
        editText = (EditText)v.findViewById(R.id.message);
        try {
            mSocket = IO.socket("http://ec2-54-180-107-241.ap-northeast-2.compute.amazonaws.com:8807");
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //mSocket.emit("message_from_client", "Server Connected");
            }
        }).on("message_from_server", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if(getActivity() == null)
                    return ;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendStr(args[0].toString(), args[1].toString());
                        }
                    });
            }
        }).on("ask_from_server", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if(getActivity() == null)
                    return ;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((roomActivity) getActivity()).openTogetherPopup(args[0].toString(), Float.parseFloat(args[1].toString()), Float.parseFloat(args[2].toString()), Float.parseFloat(args[3].toString()));
                        }
                    });
            }
        }).on("path_from_server", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        if(getActivity() == null)
                            return ;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(((roomActivity) getActivity()).getIsDrawing()) {
                                    ((roomActivity) getActivity()).drawByReceivedData(Float.parseFloat(args[0].toString()), Float.parseFloat(args[1].toString()));
                                }
                            }
                        });
                    }
        }).on("stop_drawing_from_server", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if(getActivity() == null)
                    return ;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(((roomActivity) getActivity()).getIsDrawing()) {
                            ((roomActivity) getActivity()).stopDrawing();
                        }
                    }
                });
            }
        });

        mSocket.connect();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                mSocket.emit("message_from_client", room, name, msg);
            }
        });
        return v;
    }

    /*

    채팅 프래그먼트를 초기화 해주는 코드이다.
    해당 파일에서 본 함수가 실행이 되는 위치에 따라 비동기 처리에 의한 오류가 생길 수 있어
    roomActivity에서 필요한 리소스들이 할당된 이후에 실행되도록 하였다.

     */
    public void initChat() {

        recyclerView = (RecyclerView)getView().findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        name = ((roomActivity)getActivity()).getUsername();
        room = ((roomActivity)getActivity()).getRoom();
        mSocket.emit("joinRoom", room);

        // specify an adapter (see also next example)
        mAdapter = new chatAdapter(l, name);
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyItemInserted(l.size() -1);
    }

    // 채팅창에 내용을 추가하는 함수이다.
    public void sendStr(String name, String txt)
    {
        chat c = new chat(name, txt);
        l.add(c);
        mAdapter.notifyItemInserted(l.size() -1);
        recyclerView.scrollToPosition(l.size()-1);
        editText.setText("");
    }

    // 같이 보기 요청을 보내는 함수이다.
    public void emitTogether(LatLng latLng, float zoom){
        mSocket.emit("ask_from_client", room, name, latLng.latitude, latLng.longitude, zoom);
    }

    // 그리기 정보를 전송하는 함수이다.
    public void emitPath(float x, float y) { mSocket.emit("path_from_client", room, x, y); }


    // 그리기 멈춤 신호를 보내는 함수이다.
    public void emitStop(){
        mSocket.emit("stop_drawing_from_client", room);
    }

}
