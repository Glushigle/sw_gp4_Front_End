package com.example.sw_gp4;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;

public class leftSlideView extends HorizontalScrollView {
    private int scroll_width;   //滑动宽度
    //private TextView tv_edit;
    private ImageButton editBtn;    //编辑按钮
    //private TextView tv_delete;
    private ImageButton deleteBtn;  //删除按钮
    private boolean isonce = false;
    private boolean isopen = false;
    private slide_listener listener;    //监听器

    public leftSlideView(Context context) {
        super(context,null);
        Log.d("view","left_slide_view0");
    }
    public leftSlideView(Context context, AttributeSet attrs) {
        super(context,attrs,0);
        Log.d("view","left_slide_view1");
    }
    public leftSlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        Log.d("view","left_slide_view2");
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        Log.d("view","onMeasure");
        if (!isonce) {
            //tv_edit = findViewById(R.id.lsTvEdit);
            //tv_delete = findViewById(R.id.lsTvDelete);
            editBtn = findViewById(R.id.lsBtnEdit);
            deleteBtn = findViewById(R.id.lsBtnDelete);
            isonce = true;
        }
    }
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
        Log.d("view","onLayout");
        if (changed) {
            this.scrollTo(0,0);
            //Log.d("view","width = "+String.valueOf(tv_edit.getMeasuredWidth()));
            //scroll_width = tv_edit.getWidth()+tv_delete.getWidth();
            Log.d("view","width = "+String.valueOf(editBtn.getMeasuredWidth()));
            scroll_width = editBtn.getWidth()+deleteBtn.getWidth(); //计算滑动宽度
        }
    }
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("view","onTouchEvent");
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                listener.onDownOrMove(this);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                changeScrollX();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l,t,oldl,oldt);
        Log.d("view","onScrollChanged");
        editBtn.setTranslationX(1);
    }
    public void changeScrollX() {
        Log.d("view","changeScrollX");
        if (getScrollX() >= (scroll_width/2)) { //滑动距离不低于滑动宽度的一半，则滑动生效
            this.smoothScrollTo(scroll_width,0);
            isopen = true;
            listener.onMenuIsOpen(this);
        }
        else {
            this.smoothScrollTo(0,0);
            isopen = false;
        }
    }
    public void openMenu() {
        Log.d("view","openMenu");
        if (isopen)
            return;
        this.smoothScrollTo(scroll_width,0);
        isopen = true;
        listener.onMenuIsOpen(this);
    }
    public void closeMenu() {
        Log.d("view","closeMenu");
        if (!isopen)
            return;
        this.smoothScrollTo(0,0);
        isopen = false;
    }
    public void setListener(slide_listener sl) {
        Log.d("view","setListener");
        listener = sl;
    }
    public interface slide_listener {
        void onMenuIsOpen(View v);
        void onDownOrMove(leftSlideView lsv);
    }
}