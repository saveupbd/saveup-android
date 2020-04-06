package com.saveup.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.saveup.android.BuildConfig;
import com.saveup.utils.AppConstant;
import com.saveup.utils.SessionSave;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by AAPBD on 11/4/17.
 */

public class CategoryIntenService extends IntentService {

    public CategoryIntenService() {
        super("DownloadService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            JSONObject params = new JSONObject();
            params.put("lang", AppConstant.defaultLanguageCode);

            //  params.put("lang", SessionSave.getSession("lang", CategoryIntenService.this));
            URL mUrl = new URL(BuildConfig.BASE_URL+"/api/category_list");
            System.out.println("strMap " +"mUrl " + mUrl + " Data " + params);

            HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
//            httpConnection.setRequestProperty("Content-length", "0");
            httpConnection.setUseCaches(false);
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setConnectTimeout(100000);
            httpConnection.setReadTimeout(100000);
            byte[] postDataBytes = params.toString().getBytes("UTF-8");
            httpConnection.getOutputStream().write(postDataBytes);

            int responseCode=httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                sendResultBroadcast(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    void sendResultBroadcast(String jsonResult) {

        Log.d("sendResultBroadcast", "sendResultBroadcast: " +jsonResult);
        SessionSave.saveSession("category_list", jsonResult, CategoryIntenService.this);
    }
}
