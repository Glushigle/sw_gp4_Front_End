package com.example.sw_gp4;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MultipartUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpsURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;
    private boolean post;
    private CookieManager cookieManager;

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
     * Install a trust manager that blindly trustS all SSL certificates.
     * The reason this code is being added is to enable developers to
     * do development using self signed SSL certificates on their web server.
     * The standard HttpsURLConnection class will throw an exception on
     * self signed certificates if this code is not run.
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
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset, boolean post, CookieManager cookieManager)
            throws IOException {
        this.charset = charset;
        this.post = post;
        this.cookieManager = cookieManager;

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(requestURL);
        httpConn = (HttpsURLConnection) url.openConnection();
        httpConn.setConnectTimeout(15000);
        HttpsURLConnection.setDefaultSSLSocketFactory(trustAllHosts(httpConn));

        // Cookies
        CookieHandler.setDefault(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        if (cookieManager.getCookieStore().getCookies().size() > 0) {
            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
            if (cookies != null) {
                for (HttpCookie cookie : cookies) {
                    Log.d("Cookie", cookie.getName() + "=" + cookie.getValue());
                    httpConn.setRequestProperty("Cookie", cookie.getName() + "=" + cookie.getValue());
                }
            }
        }

        if(post){
            httpConn.setDoOutput(true);    // true indicates POST method
            httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                    true);
        }
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();
        if(post){
            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();
        }

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpsURLConnection.HTTP_OK) {

            // Set cookies
            List<String> cookiesHeader = httpConn.getHeaderFields().get("Set-Cookie");
            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    cookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        return response;
    }
}