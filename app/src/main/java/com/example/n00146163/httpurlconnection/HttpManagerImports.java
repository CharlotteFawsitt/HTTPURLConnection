package com.example.n00146163.httpurlconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by n00146163 on 13/02/2018.
 */

public class HttpManagerImports {

    public static String getData(String uri) {
        BufferedReader reader = null;

        try {
            //Creates a new url
            URL url = new URL(uri);

            //creates a connection to the url that has been given
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //builds a string based on the information at the url
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}
