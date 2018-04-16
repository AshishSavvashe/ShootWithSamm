package com.shootwithsam.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SwatiK on 20-08-2017.
 */
public class SecuredNetworkUtility {
    private static final String TAG = "NetworkCheckUtility";
    public static Context context;

    /**
     * Send input data to the requested url and get the response.
     *
     * @param targetURL
     * @param postDataParams
     * @return
     */
    public static String sendPostData(String targetURL, JSONObject postDataParams) {

        HttpURLConnection connection = null;
        try {
            Log.d(TAG, postDataParams.toString());

            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection(); // Creating
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);

            // connection
            // object
            connection.setRequestMethod("POST"); // Way of submitting data(e.g.
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json"); // Setting content type- JSON

            // set it to true if you want to send outputin the request body
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(postDataParams.toString());
            writer.flush();
            writer.close();

            connection.connect();

            BufferedReader bufferResponse;
            // Retrieving response from the webservice
            if (connection.getResponseCode() == 200) {
                bufferResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                bufferResponse = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String line;
            StringBuffer response = new StringBuffer();
            while ((line = bufferResponse.readLine()) != null) {
                response.append(line);
                response.append("\r");
            }

            os.close();
            bufferResponse.close();
            return response.toString().trim();
        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Generate url params
     */
    public static String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static String getSecuredData(String url) throws Exception {
        HttpURLConnection con = null;
        try {
            Log.d(TAG, "URL : " + url);

            URL urlObj = new URL(url);
            con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("GET");
            con.setUseCaches(false);
            con.setReadTimeout(15000);
            con.setDoInput(true);
            con.setDoOutput(true);
            // Get event details..
            InputStream is = con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
