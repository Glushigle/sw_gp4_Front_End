package com.example.sw_gp4;

import java.util.ArrayList;

public class Group
{
    String group_name;
    String group_id;
    String owner_username = "";
    boolean im_leader = false;
    boolean invitation;
    String info;
    int color_id;
    int id;
    ArrayList<String> member;
    DDLForGroup firstTask;
    /*
    Group(String i, String n)
    {
        member = new ArrayList<String>();
        group_name = n;
        group_id = i;
        id = Integer.parseInt(i);
    }
    Group(int i, String n)
    {
        member = new ArrayList<String>();

        group_name = n;
        id = i;
        group_id = Integer.toString(i);
    }
    Group(String i, String n, String name)
    {
        member = new ArrayList<String>();

        group_name = n;
        group_id = i;
        id = Integer.parseInt(i);
        member.add(name);
    }
    Group(int i, String n, String name)
    {
        member = new ArrayList<String>();

        group_name = n;
        id = i;
        group_id = Integer.toString(i);
        member.add(name);
    }*/
    Group(String group_id, String group_name,String info, int color_id, DDLForGroup firstTask, boolean invitation) // String owner_id,
    {
        this.group_name = group_name;
        this.group_id = group_id;
        this.id = Integer.parseInt(group_id);
        this.info = info;
        this.color_id = color_id;
        this.firstTask = firstTask;
        this.invitation = invitation;
    }

    public void setOwner(String owner_username){
        this.owner_username = owner_username;
        this.im_leader = (User.username.equals(owner_username));
    }
}