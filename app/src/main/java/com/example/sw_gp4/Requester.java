package com.example.sw_gp4;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class Requester {

    /**
     * Intended to be used for other POST & GET requests.
     * See https://github.com/Glushigle/sw_gp4_Front_End#postrequester-用法 for usage.
     */

    private static final String charset = "UTF8";

    public static String get(String full_url, String[] keys, String[] values) {

        final String TAG = "get";
        full_url = URLBuilder.build(full_url, keys, values);
        Log.i(TAG, "get "+full_url);
        return request(full_url, new String[0], new String[0], false);
    }

    public static String post(String full_url, String[] keys, String[] values){

        final String TAG = "post";
        Log.i(TAG, "post "+keys.toString()+" "+values.toString());
        return request(full_url, keys, values, true);
    }

    private static String request(String full_url, String[] keys, String[] values, boolean post){

        final String TAG = "request";
        String rtn = "";
        try {
            MultipartUtility multipart = new MultipartUtility(full_url, charset, post);

            if(post){
                for (int i = 0; i < keys.length; i++) {
                    multipart.addFormField(keys[i], values[i]);
                }
            }

            List<String> response = multipart.finish();
            Log.i(TAG, "SERVER REPLIED:");
            for (String line : response) {
                Log.i(TAG, "Request Response:::" + line);
                //responseString = line;
            }
            rtn = response.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }
    /**
    private class GetJSONTask extends AsyncTask<String, Void, String> {
        //private ProgressDialog pd;

        // onPreExecute called before the doInBackgroud start for display
        // progress dialog.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pd = ProgressDialog.show(MainActivity.this, "", "Loading", true,
                    false); // Create and show Progress dialog
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                return Utility.downloadDataFromUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the doInBackgroud and also we
        // can hide progress dialog.
        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            tvData.setText(result);
        }
    }*/
}
