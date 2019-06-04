package com.example.sw_gp4;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class leftSlideAdapter extends RecyclerView.Adapter<leftSlideAdapter.VH> implements
        leftSlideView.slide_listener {
    public class VH extends RecyclerView.ViewHolder {
        public ImageButton editBtn; //编辑按钮
        public ImageButton deleteBtn;   //删除按钮
        public TextView tv_title;   //标题
        public TextView tv_description; //描述
        public TextView tv_time;    //时间
        public LinearLayout ll_text;    //DDL文本部分的父布局，包括标题、时间和描述

        public VH(View v) {
            super(v);
            editBtn = v.findViewById(R.id.lsBtnEdit);
            deleteBtn = v.findViewById(R.id.lsBtnDelete);
            tv_title = v.findViewById(R.id.lsTvTitle);
            tv_description = v.findViewById(R.id.lsTvDescription);
            tv_time = v.findViewById(R.id.lsTvTime);
            ll_text = v.findViewById(R.id.lsLilayoutText);
            ((leftSlideView)v).setListener(leftSlideAdapter.this);
        }
    }

    private Context context;
    private List<DDLText> data; //DDL数据
    private int dsize;  //数据集大小
    private int max_width;  //布局宽度
    private slideViewClickListener clickListener;   //监听器
    private leftSlideView menu = null;  //菜单被打开的左滑条

    public leftSlideAdapter(Context cont, List<DDLText> dt, int mw) {
        Log.d("adapter","left_slide_adapter");
        context = cont;
        clickListener = (slideViewClickListener)cont;
        data = new ArrayList<>(dt);
        dsize = data.size();
        max_width = mw;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("adapter","onCreateViewHolder"+String.valueOf(viewType));
        View v = LayoutInflater.from(context).inflate(R.layout.left_slide_view,parent,false);
        return new VH(v);
    }
    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {
        Log.d("adapter","onBindViewHolder"+String.valueOf(position));
        LinearLayout ll_tmp = (LinearLayout)holder.ll_text.getParent();
        ll_tmp.setBackground(context.getDrawable(R.drawable.border));
        //ll_tmp.setBackgroundResource(0);
        final DDLText ddl = data.get(position);   //获取ddl信息
        if (ddl.ddl_status.equals("1")) {
            holder.tv_title.setBackgroundColor(Color.rgb(192,192,192));
            holder.tv_time.setBackgroundColor(Color.rgb(192,192,192));
            holder.tv_description.setBackgroundColor(Color.rgb(192,192,192));
        }
        else {
            int color = ColorConverter.fromId(position);
            holder.tv_title.setBackgroundColor(color);
            holder.tv_time.setBackgroundColor(color);
            holder.tv_description.setBackgroundColor(color);
        }
        String str = "截止时间："+ ddl.ddl_time;
        holder.tv_time.setText(str);    //时间
        holder.tv_time.setWidth(max_width);
        holder.tv_title.setText(ddl.ddl_title);    //标题
        holder.tv_title.setWidth(max_width);
        holder.tv_description.setWidth(max_width);    //描述
        str = ddl.ddl_description;
        if (str == null)
            holder.tv_description.setVisibility(View.GONE);
        else
            holder.tv_description.setText(str);
        /*final leftSlideView lsview = (leftSlideView) holder.ll_text.getParent().getParent();
        final ViewTreeObserver observer = lsview.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int tmp = lsview.getWidth();
                Log.d("adapter","set width = "+String.valueOf(tmp)+","+String.valueOf(position));
                holder.ll_text.getLayoutParams().width = tmp;
                holder.ll_text.requestLayout();
                //observer.removeOnGlobalLayoutListener(this);
                lsview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });*/
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                clickListener.onEditClick(v,pos,leftSlideAdapter.this);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                clickListener.onDeleteClick(v,pos,leftSlideAdapter.this);
            }
        });
        holder.ll_text.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (ddl.ddl_status.equals("0")) {
                    int pos = holder.getLayoutPosition();
                    holder.tv_title.setBackgroundColor(Color.rgb(192,192,192));
                    holder.tv_time.setBackgroundColor(Color.rgb(192,192,192));
                    holder.tv_description.setBackgroundColor(Color.rgb(192,192,192));
                    clickListener.onItemClick(v,pos,leftSlideAdapter.this);
                }
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        Log.d("adapter","getItemCount"+String.valueOf(data.size()));
        return data.size();
    }
    public void removeData(int position) {
        Log.d("adapter","removeData"+String.valueOf(position));
        data.remove(position);
        dsize = data.size();
        notifyItemRemoved(position);
    }
    public void addData(DDLText ddl) {
        data.add(ddl);
        dsize = data.size();
        Log.d("adapter","addData");
        Log.d("adapter","notifyinsert");
        notifyItemInserted(dsize-1);
    }
    public DDLText getData(int position) {
        return data.get(position);
    }
    public void changeDate(int position, DDLText ddl) {
        data.set(position,ddl);
        notifyItemChanged(position);
    }
    public void onMenuIsOpen(View v) {
        Log.d("adapter","onMenuIsOpen");
        menu = (leftSlideView) v;
    }
    public void onDownOrMove(leftSlideView lsv) {
        Log.d("adapter","onDownOrMove");
        if (isMenuOpen() && menu != lsv) {
            closeMenu();
        }
    }
    public boolean isMenuOpen() {
        Log.d("adapter","isMenuOpen");
        if (menu == null)
            return false;
        return true;
    }
    public void closeMenu() {
        Log.d("adapter","closeMenu");
        menu.closeMenu();
        menu = null;
    }
    public interface slideViewClickListener {
        void onItemClick(View v, int position, leftSlideAdapter adapter);
        void onDeleteClick(View v, int position, leftSlideAdapter adapter);
        void onEditClick(View v, int position, leftSlideAdapter adapter);
    }
}