package com.example.sw_gp4;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
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

import org.json.JSONObject;

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

    private void markInvitation(View convertView, final int position){
        convertView.findViewById(R.id.group_name).setAlpha((float)0.5);
        convertView.findViewById(R.id.group_color).setAlpha((float)0.5);
        convertView.findViewById(R.id.group_first_task).setVisibility(View.GONE);
        ((TextView)convertView.findViewById(R.id.group_name)).setGravity(Gravity.CENTER_VERTICAL);

        ((TextView)convertView.findViewById(R.id.group_name)).setWidth(450);

        Button btn_deny = (Button)convertView.findViewById(R.id.btn_deny);
        Button btn_accept = (Button)convertView.findViewById(R.id.btn_accept);
        btn_deny.setVisibility(View.VISIBLE);
        btn_accept.setVisibility(View.VISIBLE);
        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] keys = {"group_id"};
                String[] values = {groups.get(position).group_id};
                try{
                    String response = Requester.post(mContext.getResources().getString(R.string.server_uri)+"deny_groupReq",keys,values);
                    JSONObject responseObj = new JSONObject(response);
                    boolean valid = responseObj.getBoolean("valid");
                    if(valid){
                        Toast.makeText(mContext, "已狠心拒绝邀请", Toast.LENGTH_SHORT).show();
                        ((GroupList)mContext).restart();
                    }
                    else{
                        String error_info = responseObj.getString("error_info");
                        Toast.makeText(mContext, "错误："+error_info, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext, "怎么可以拒绝邀请！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] keys = {"group_id"};
                String[] values = {groups.get(position).group_id};
                try{
                    String response = Requester.post(mContext.getResources().getString(R.string.server_uri)+"join_group",keys,values);
                    JSONObject responseObj = new JSONObject(response);
                    boolean valid = responseObj.getBoolean("valid");
                    if(valid){
                        Toast.makeText(mContext, "成功加入小组！", Toast.LENGTH_SHORT).show();
                        ((GroupList)mContext).restart();
                    }
                    else{
                        String error_info = responseObj.getString("error_info");
                        Toast.makeText(mContext, "错误："+error_info, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext, "不好意思，后会有期", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void fillValues(final int position, View convertView) {
        Group group = groups.get(position);

        // Appearance of group item
        ImageButton color_button = (ImageButton) convertView.findViewById(R.id.group_color);
        ((GradientDrawable) color_button.getBackground()).setColor(ColorConverter.fromId(group.id));
        ((TextView) convertView.findViewById(R.id.group_name)).setText(group.group_name);
        if(group.firstTask!=null) {
            Toast.makeText(mContext,"DEBUG",Toast.LENGTH_SHORT);
            ((TextView) convertView.findViewById(R.id.group_first_task)).setText(group.firstTask.time.split(" ")[0]+" "+group.firstTask.title);
            ((TextView) convertView.findViewById(R.id.group_first_task)).setVisibility(View.VISIBLE);
        }
        else{
            ((TextView) convertView.findViewById(R.id.group_first_task)).setVisibility(View.INVISIBLE);
        }

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

        if(group.im_leader)
            ((View)convertView.findViewById(R.id.leader_sign)).setVisibility(View.VISIBLE);

        if(group.invitation)
            markInvitation(convertView, position);
        else {
            // 因为invitation会reset这些，只好再处理一次
            ((Button)convertView.findViewById(R.id.btn_deny)).setVisibility(View.INVISIBLE);
            ((Button)convertView.findViewById(R.id.btn_accept)).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.group_name).setAlpha(1);
            convertView.findViewById(R.id.group_color).setAlpha(1);
            convertView.setClickable(true);
        }

        // Delete
        convertView.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
