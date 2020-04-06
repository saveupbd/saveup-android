package com.saveup.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.saveup.utils.SessionSave;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author AAPBD on 13,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class PayPayUserInfoService extends IntentService
{
    public PayPayUserInfoService() {
        super("PayPayUserInfoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String response = "";
        try {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://api.sandbox.paypal.com/v1/identity/openidconnect/userinfo/?schema=openid");
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Authorization", "Bearer "+ SessionSave.getSession("token_id", PayPayUserInfoService.this));
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    response = "";
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    response = "";
                }
                response = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                response = "";

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
//
            System.out.println("response " + response);
             sendResultBroadcast(response);

        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void sendResultBroadcast(String jsonResult) {

        Log.d("sendResultBroadcast", "sendResultBroadcast: " +jsonResult);
        SessionSave.saveSession("paypal_user_info", jsonResult, PayPayUserInfoService.this);

    }
}

