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

/**
 * @author AAPBD on 11,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class CountryIntenCityService extends IntentService {

    public CountryIntenCityService() {
        super("DownloadService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            JSONObject params = new JSONObject();
            params.put("lang", AppConstant.defaultLanguageCode);

         //   params.put("lang", SessionSave.getSession("lang", CountryIntenCityService.this));

            URL mUrl = new URL(BuildConfig.BASE_URL+"/api/country_city_list");
            HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setUseCaches(false);
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setConnectTimeout(100000);
            httpConnection.setReadTimeout(100000);
            byte[] postDataBytes = params.toString().getBytes("UTF-8");
            httpConnection.getOutputStream().write(postDataBytes);

            Log.wtf("cray", " CountryIntenCityService "+params.toString());


            int responseCode = httpConnection.getResponseCode();

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


    void sendResultBroadcast(String jsonResult) {

        SessionSave.saveSession("countrylist", jsonResult, CountryIntenCityService.this);
    }
}
