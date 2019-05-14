package com.example.sw_gp4;

import java.util.ArrayList;

public class Group
{
    String group_name;
    String group_id;
    int id;
    ArrayList<String> member;
    Group(String i, String n)
    {
        group_name = n;
        group_id = i;
        id = Integer.parseInt(i);
    }
    Group(int i, String n)
    {
        group_name = n;
        id = i;
        group_id = Integer.toString(i);
    }
    Group(String i, String n, String name)
    {
        group_name = n;
        group_id = i;
        id = Integer.parseInt(i);
        member.add(name);
    }
    Group(int i, String n, String name)
    {
        group_name = n;
        id = i;
        group_id = Integer.toString(i);
        member.add(name);
    }
}