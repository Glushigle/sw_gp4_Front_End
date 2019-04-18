package com.example.myapplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMsgs();//自定义初始化数据的函数

        inputText = (EditText)findViewById(R.id.input_text);
        send = (Button)findViewById(R.id.send);
        msgRecyclerView = (RecyclerView)findViewById(R.id.msg_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        /*也许你做过FruitAdapter，那理解起这段代码来就会很轻松了，
         * 但是为了面向更多像博主一样的初学者（初学者难免会遇到一些很简单的甚至于大神都懒得回答的问题），就说的明白点。
         *ListView可以实现上下滚动，但是不能实现横向滚动(例如微信选择小程序时的那个横向滚动)，但是RecyclerView能够实现。
         * 原因：ListView的布局排列是由自身去管理的，而RecyclerView则将这个工作交给了LayoutManager，
         * LayoutManager中制定了一套可扩展的布局排列接口，子类只要按照接口的规范来实现，就能定制出各种不同排列方式的布局了。
         * 这个程序我们使用了LinearLayoutManager这种线性的布局排列*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                //if(!"".equals(content)){
                    Msg msg = new Msg(content,Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size()-1);//当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size()-1);//将ListView定位到最后一行
                    inputText.setText("");//消息发出后清空输入框中的内容
                //}
            }
        });
    }//事件响应
    private void initMsgs(){
        Msg msgl = new Msg("Hello guy.",Msg.TYPE_RECEICED);
        msgList.add(msgl);
        Msg msg2 = new Msg("Hello.Who is that?",Msg.TYPE_SEND);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is Tom.Nice talking to you.",Msg.TYPE_RECEICED);
        msgList.add(msg3);
    }
}
