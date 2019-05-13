package com.example.sw_gp4;

// Please download org.json.jar for this
// TODO In-project dependencies?
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
//import javax.net.ssl.SSLSocketFactory;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

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

    // Create a trust manager that does not validate certificate chains
    private static final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) {
        }
    } };

    /**
     * This function will install a trust manager that will blindly trust all SSL certificates.  The reason this code is being added is to enable developers to do development using self signed SSL certificates on their web server. The standard HttpsURLConnection class will throw an exception on self signed certificates if this code is not run.
     */
    private static SSLSocketFactory trustAllHosts(HttpsURLConnection connection){
        SSLSocketFactory oldFactory=connection.getSSLSocketFactory();
        try {
            SSLContext sc=SSLContext.getInstance("TLS");
            sc.init(null,trustAllCerts,new java.security.SecureRandom());
            SSLSocketFactory newFactory=sc.getSocketFactory();
            connection.setSSLSocketFactory(newFactory);
        }
        catch (  Exception e) {
            e.printStackTrace();
        }
        return oldFactory;
    }
    /**
     * Intended to be used for other POST requests.
     * @param full_url The full url of the API
     * @param keys The keys of key-value pairs of the query
     * @param values Same # as keys
     * @return response in String
     */
    public static String request(String full_url, String[] keys, String[] values) {

        /** Note: "/** Enter" generates the template for function description. **/
        // TODO Ask for get_group_list(user_id) API from backend.
        // TODO How to obtain user_id locally? cookies!
        // https://www.wikihow.com/Execute-HTTP-POST-Requests-in-Android
        // https://stackoverflow.com/questions/43538954/how-to-get-response-after-using-post-method-in-android

        HttpsURLConnection client = null;
        String response = "";
        try {

            // Certificate issue
            HttpsURLConnection.setDefaultSSLSocketFactory(trustAllHosts(client));
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });

            URL url = new URL(full_url);
            client = (HttpsURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "application/json");

            client.setDoInput(true);
            client.setDoOutput(true);
            client.setConnectTimeout(15000);

            client.connect();

            OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            byte[] query = convertKvpToByte(keys, values);
            outputPost.write(query);


            InputStream inputStream = new BufferedInputStream(client.getInputStream());
            response = convertStreamToString(inputStream);
            outputPost.flush();
            outputPost.close();

        } catch (Exception e){
            e.printStackTrace(); // TODO handle exception

        } finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }
        return response;
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
