package com.example.sw_gp4;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class Requester {

    /**
     * Intended to be used for other POST & GET requests.
     * See https://github.com/Glushigle/sw_gp4_Front_End#requester用法 for usage.
     */
    private static final String TAG = "Requester";
    private static final String charset = "UTF8";
    private static final java.net.CookieManager cookieManager = new java.net.CookieManager();

    public static String get(String full_url, String[] keys, String[] values) {

        full_url = URLBuilder.build(full_url, keys, values);
        Log.i(TAG, "get "+full_url);
        return request(full_url, new String[0], new String[0], false);
    }

    public static String post(String full_url, String[] keys, String[] values){

        Log.i(TAG, "post "+full_url);
        logParams(keys, values);
        return request(full_url, keys, values, true);
    }

    private static void logParams(String[] keys, String[] values){
        for(int i=0; i<keys.length; ++i){
            Log.d(TAG, "key="+keys[i]+"; value="+values[i]);
        }
    }

    private static String request(String full_url, String[] keys, String[] values, boolean post){

        String rtn = "";
        try {
            MultipartUtility multipart = new MultipartUtility(full_url, charset, post, cookieManager);

            if(post){
                for (int i = 0; i < keys.length; i++) {
                    multipart.addFormField(keys[i], values[i]);
                }
            }

            List<String> response = multipart.finish();
            for (String line : response) {
                Log.i(TAG, "Request Response:::" + line);
            }
            rtn = response.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    // The following code is for async request, but we don't need it for now.
    private static String full_url;
    private static String[] keys;
    private static String[] values;
    private static boolean post;
    private static void construct(String full_url, String[] keys, String[] values, boolean post){
        Requester.full_url = full_url;
        Requester.keys = keys;
        Requester.values = values;
        Requester.post = post;
    }
    private static class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return null;
            //return request();
        }
    }
}
