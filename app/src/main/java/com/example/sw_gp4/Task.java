package com.example.sw_gp4;

import java.util.Locale;

public class Task {
    public String id;
    public String title;
    public int[] finish_time; //{2019,5,20,13,0,0}
    public int status;
    public String info;
    public int color;
    public int publicity;
    public Task(){
        this.title = this.id = this.info = "";
        this.finish_time = null;
        this.status = 0;
        this.color = 0;
        this.publicity =0;
    }
    public Task(String id, String  title, String finish_time, int status, String info, int publicity){
        this.id = id;
        this.title = title;
        this.finish_time = timeParser(finish_time);
        this.status = status;
        this.info = info;
        this.color = ColorConverter.fromId(id);
        this.publicity = publicity;
    }
    private int[] timeParser(String finish_time){
        String [] dt = finish_time.split(" ");  //"2019-05-20 13:00:00"
        String[] d = dt[0].split("-");          //"2019-05-20"
        String[] t = dt[1].split(":");          //"13:00:00"
        int[] rtn = {Integer.valueOf(d[0]),Integer.valueOf(d[1]),Integer.valueOf(d[2]),
                Integer.valueOf(t[0]),Integer.valueOf(t[1]),Integer.valueOf(t[2])};
        return rtn;
    }

    public static String getZhDate(String finish_time){
        String [] dt = finish_time.split(" ");  //"2019-05-20 13:00:00"
        String[] d = dt[0].split("-");          //"2019-05-20"
        return d[0]+" 年 "+d[1]+" 月 "+d[2]+" 日";
    }
    public static String getZhDate(int[] dt){
        return String.format(Locale.CHINESE,"%d 年 %d 月 %d 日",dt[0],dt[1],dt[2]);
    }
    public static String getZhDate(int y, int M, int d){
        return String.format(Locale.CHINESE,"%d 年 %d 月 %d 日",y,M,d);
    }

    public static  String getZhTime(String finish_time){
        String [] dt = finish_time.split(" ");  //"2019-05-20 13:00:00"
        String[] t = dt[1].split(":");          //"2019-05-20"
        return t[0] + " : " + t[1];
    }
    public static  String getZhTime(int[] dt){
        return String.format(Locale.CHINESE,"%02d : %02d",dt[3],dt[4]);
    }
    public static  String getZhTime(int h, int m){
        return String.format(Locale.CHINESE,"%02d : %02d",h,m);
    }
}
