package com.example.sw_gp4;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.BufferedOutputStream;

public class GroupList extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    /**
     * Helper function to convert kvp to query for POST.
     * @param keys
     * @param values
     * @return query in byte[]
     */
    private byte[] createQuery(String[] keys, String[] values){
        byte[] query = null;
        JSONObject queryObj = new JSONObject();
        try {
            for(int i=0; i<keys.length; ++i){
                queryObj.put(keys[i], values[i]);
            }
            query = queryObj.toString().getBytes("UTF8"); // TODO Allow configuration
        } catch (Exception e) {
            e.printStackTrace(); // TODO handle exception
        }
        return query;
    }

    /** Note: "/** Enter" generates the template for function description. **/
    /**
     * Intended to be used for other POST requests.
     * TODO Ask for get_group_list(user_id) API from backend.
     * TODO How to obtain user_id locally?
     * https://www.wikihow.com/Execute-HTTP-POST-Requests-in-Android
     * @param full_url User requesting group_list
     * @param keys The keys of key-value pairs of the query
     * @param values Same # as keys
     * @return group_list in String
     */
    private String postRequest(String full_url, String[] keys, String[] values) {

        String rtn = "";
        URL url = new URL(full_url);
        HttpURLConnection client = null;
        try {
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setDoOutput(true);

            OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            byte[] query = createQuery(keys, values);
            outputPost.write(query);
            outputPost.flush();
            outputPost.close();

        } catch (Exception e){
            e.printStackTrace(); // TODO handle exception

        } finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }
        return rtn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
