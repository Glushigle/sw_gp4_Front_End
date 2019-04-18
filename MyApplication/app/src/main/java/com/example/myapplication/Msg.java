package com.example.myapplication;

public class Msg {
    public static  final int TYPE_RECEICED = 0;
    public static  final int TYPE_SEND = 1;
    private String content;
    private int type;
    public Msg(String content,int type){
        this.content = content;
        this.type = type;
    }
    public String getContent(){
        return content;
    }//在后面设置文本内容时调用
    public int getType(){
        return type;
    }//条件语句的判断依据
}
