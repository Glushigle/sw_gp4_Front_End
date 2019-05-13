package com.example.sw_gp4;
import android.util.Log;

import java.util.List;

public class PostRequester {
    /*
    // Sample to use this
    String[] keys = {"key1"};
    String[] values = {"value1"};
    String response = PostRequester.request("full_url", keys, values);
    */

    static final String charset = "UTF8";

    /**
     * Intended to be used for other POST requests.
     * See https://github.com/Glushigle/sw_gp4_Front_End#postrequester-用法 for usage.
     */
    public static String request(String full_url, String[] keys, String[] values){

        final String TAG = "request";

        /** Note: "/** Enter" generates the template for function description. **/
        // TODO Ask for get_group_list(user_id) API from backend.
        // TODO How to obtain user_id locally? cookies!

        String rtn = "";
        try{
            MultipartUtility multipart = new MultipartUtility(full_url, charset);

            for (int i = 0; i < keys.length; i++) {
                multipart.addFormField(keys[i], values[i]);
            }
            List<String> response = multipart.finish();
            Log.e(TAG, "SERVER REPLIED:");
            for (String line : response) {
                Log.e(TAG, "Request Response:::" + line);
                //responseString = line;
            }
            rtn = response.get(0);
        } catch (Exception e){
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
