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

        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
                ((GroupList) mContext).OnDeleteClicked(view, position);
            }
        });
        return v;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        ImageButton color_button = (ImageButton) convertView.findViewById(R.id.group_color);
        Button text_button = (Button) convertView.findViewById(R.id.group_text);

        Group group = groups.get(position);
        ((GradientDrawable) color_button.getBackground()).setColor(
                ContextCompat.getColor(mContext,group.color_id));
        text_button.setText(group.group_name);

        //todo: mark group leader:
        //if(group.im_leader)
        //    ((View)convertView.findViewById(R.id.leader_sign)).setVisibility(View.VISIBLE);

        // TODO: click button -> adapter perform click
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
