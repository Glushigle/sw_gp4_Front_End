package com.example.sw_gp4;

import android.graphics.Color;

import java.util.Random;

public class ColorConverter {
    static final int A = 201;
    static final int B = 237;
    /* RGB: 1个B; 1个A; 1个A<x<B
    =>  id%3 得B的位置,
        id%2 得剩下2个位置里A的前后
        id/6%77 map进[A, B]间
    */
    public static int fromId(int id){
        int[] RGB = {-1,-1,-1};
        RGB[id%3] = B;
        if(RGB[id%2]<0) RGB[id%2] = A;
        else RGB[id%2+1] = A;
        for(int i=0; i<3; ++i){ if(RGB[i]<0) RGB[i] = A+(id/6)%(B-A); }

        return Color.argb(255, RGB[0], RGB[1], RGB[2]);
    }
    public static int fromId(String id_str){
        return fromId(Integer.parseInt(id_str));
    }
    public static int fromName(String username){
        int fake_id = 0;
        Random random = new Random();
        for(char c:username.toCharArray()){
            try{
                fake_id += ((int)c)*193;
            } catch (Exception e) {
                fake_id += (random.nextInt(127));
            }
        }
        return fromId(fake_id);
    }
}
