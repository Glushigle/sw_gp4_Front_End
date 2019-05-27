package com.example.sw_gp4;

import android.graphics.Color;

import java.util.Random;

public class ColorConverter {
    public static int id2color(int id){
        final int A = 201;
        final int B = 237;
            /* RGB: 1个255; 1个178; 1个178<x<255
            =>  id%3 得255的位置,
                id%2 得剩下2个位置里178的前后
                id/6%77 map进[178, 255]间
            */
        int[] RGB = {-1,-1,-1};

        RGB[id%3] = B;
        if(RGB[id%2]<0) RGB[id%2] = A;
        else RGB[id%2+1] = A;
        for(int i=0; i<3; ++i){ if(RGB[i]<0) RGB[i] = A+(id/6)%(B-A); }

        return Color.argb(255, RGB[0], RGB[1], RGB[2]);
    }
    public static int id2color(String id_str){
        return id2color(Integer.parseInt(id_str));
    }
    public static int username2color(String username){
        int fake_id = 0;
        Random random = new Random();
        for(char c:username.toCharArray()){
            try{
                fake_id += ((int)c)*19;
            } catch (Exception e) {
                fake_id += (random.nextInt(127));
            }
        }
        return id2color(fake_id);
    }
}
