package com.example.sw_gp4;

// Please download org.json.jar for this
// TODO In-project dependencies?
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequester {
    /*
    // Sample to use this
    String[] keys = {"key1"};
    String[] values = {"value1"};
    String response = PostRequester.request("full_url", keys, values);
    */

    /**
     * Helper function for postRequest: Convert kvp to query for POST.
     */
    private static byte[] convertKvpToByte(String[] keys, String[] values){

        byte[] query = null;
        JSONObject queryObj = new JSONObject();
        try {
            for(int i=0; i<keys.length; ++i){
                queryObj.put(keys[i], values[i]);
            }
            query = queryObj.toString().getBytes("UTF8"); // TODO Allow configuration
        } catch (Exception e) {
            e.printStackTrace(); // TODO Handle exception
        }
        return query;
    }

    /** Helper function for postRequest
     */
    private static String convertStreamToString(InputStream is) {

        // TODO Confirm the return type with backend
        // From https://stackoverflow.com/questions/43538954/how-to-get-response-after-using-post-method-in-android

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * Intended to be used for other POST requests.
     * @param full_url User requesting group_list
     * @param keys The keys of key-value pairs of the query
     * @param values Same # as keys
     * @return group_list in String
     */
    public static String request(String full_url, String[] keys, String[] values) {

        /** Note: "/** Enter" generates the template for function description. **/
        // TODO Ask for get_group_list(user_id) API from backend.
        // TODO How to obtain user_id locally?
        // https://www.wikihow.com/Execute-HTTP-POST-Requests-in-Android
        // https://stackoverflow.com/questions/43538954/how-to-get-response-after-using-post-method-in-android

        HttpURLConnection client = null;
        String response = "";
        try {
            URL url = new URL(full_url);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);
            client.setConnectTimeout(15000);
            client.setRequestProperty("Content-Type", "application/json");
            client.connect();

            OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            byte[] query = convertKvpToByte(keys, values);
            outputPost.write(query);
            outputPost.flush();
            outputPost.close();

            InputStream inputStream = new BufferedInputStream(client.getInputStream());
            response = convertStreamToString(inputStream);

        } catch (Exception e){
            e.printStackTrace(); // TODO handle exception

        } finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }
        return response;
    }
}
