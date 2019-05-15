package com.example.sw_gp4;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

class URLBuilder {
    static private String host;
    static private StringBuilder params;

    static public String build(String host, String[] keys, String[] values){
        String url = host;
        params = new StringBuilder();
        for(int i=0; i<keys.length; ++i){
            addParameter(keys[i], values[i]);
        }
        return host + '?' + params.toString();
    }

    static private void addParameter(String parameter, String value) {
        if(params.toString().length() > 0){params.append("&");}
        params.append(parameter);
        params.append("=");
        params.append(value);
    }
}