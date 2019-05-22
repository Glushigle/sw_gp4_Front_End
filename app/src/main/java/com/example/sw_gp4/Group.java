package com.example.sw_gp4;

import java.util.ArrayList;

public class Group
{
    String group_name;
    String group_id;
    String owner_id; // todo: should be owner_username
    boolean im_leader; // todo: I'm Leader -> mark leader_sign
    boolean invitation; // todo: if it's an invitation
    String info;
    int color_id;
    int id;
    ArrayList<String> member;
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
    }
    Group(String group_id, String group_name, String owner_id, String info, int color_id)
    {
        this.group_name = group_name;
        this.group_id = group_id;
        this.id = Integer.parseInt(group_id);
        this.owner_id = owner_id;
        this.info = info;
        this.color_id = color_id;
    }
}