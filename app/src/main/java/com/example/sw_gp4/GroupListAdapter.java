package com.example.sw_gp4;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;

public class GroupListAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private ListView mListView;
    private ArrayList<Group> groups;

    public GroupListAdapter(Context mContext, ArrayList<Group> groups, ListView mListView) {
        this.mContext = mContext;
        this.mListView = mListView;
        resetData(groups);
    }

    public void resetData( ArrayList<Group> groups){
        this.groups = groups;
        notifyDataSetChanged();
        closeAllItems();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, null);
        return v;
    }

    private void markInvitation(View convertView){
        convertView.findViewById(R.id.group_text).setAlpha((float)0.5);//.setBackgroundColor(bg_color);
        convertView.findViewById(R.id.group_color).setAlpha((float)0.5);

        ((Button)convertView.findViewById(R.id.group_text)).setWidth(450);
        ((Button)convertView.findViewById(R.id.btn_deny)).setVisibility(View.VISIBLE);
        ((Button)convertView.findViewById(R.id.btn_accept)).setVisibility(View.VISIBLE);
    }

    @Override
    public void fillValues(final int position, View convertView) {
        Group group = groups.get(position);

        // Appearance of group item
        ImageButton color_button = (ImageButton) convertView.findViewById(R.id.group_color);
        Button text_button = (Button) convertView.findViewById(R.id.group_text);
        ((GradientDrawable) color_button.getBackground()).setColor(
                ContextCompat.getColor(mContext,group.color_id));
        text_button.setText(group.group_name);

        // Group item clicked: share the listener with mListView
        ImageButton.OnClickListener fakeItemClick = new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                mListView.performItemClick(
                        mListView.getAdapter().getView(position, null, null),
                        position,
                        mListView.getAdapter().getItemId(position)
                );
            }
        };
        color_button.setOnClickListener(fakeItemClick);
        text_button.setOnClickListener(fakeItemClick);

        //todo: Mark group leader
        //if(group.im_leader)
        //    ((View)convertView.findViewById(R.id.leader_sign)).setVisibility(View.VISIBLE);

        //todo: Mark group invitation
        //if(group.invitation)
            markInvitation(convertView);

        // Delete
        convertView.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
                ((GroupList) mContext).OnDeleteClicked(view, position);
            }
        });
    }

    @Override
    public int getCount() {
        if(groups==null) return 0;
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
