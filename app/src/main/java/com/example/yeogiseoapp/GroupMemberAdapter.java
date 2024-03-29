package com.example.yeogiseoapp;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*
해당 그룹에 속한 멤버들의 정보를 받아온 뒤
이를 화면에 나타내기 위한 GroupMember Adapter클래스이다.
 */
public class GroupMemberAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<GroupMemberItem> groupMemberList = new ArrayList<GroupMemberItem>() ;

    // ListViewAdapter의 생성자
    public GroupMemberAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return groupMemberList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.popup_groupmember_attribute, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView name = (TextView) convertView.findViewById(R.id.groupMemberListName) ;
        TextView email = (TextView) convertView.findViewById(R.id.groupMemberListEmail) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        GroupMemberItem groupMemberInfo = groupMemberList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        name.setText(groupMemberInfo.getName());
        email.setText(groupMemberInfo.getEmail());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return groupMemberList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String n, String e) {
        GroupMemberItem item = new GroupMemberItem();

        item.setName(n);
        item.setEmail(e);

        groupMemberList.add(item);
    }
}
