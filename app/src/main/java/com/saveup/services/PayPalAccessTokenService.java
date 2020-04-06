package com.saveup.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import com.saveup.utils.SessionSave;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author AAPBD on 13,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.model
 */


public class PayPalAccessTokenService extends IntentService {
    public PayPalAccessTokenService() {
        super("PayPalAccessTokenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        URL url;
        String response = "", CONFIG_CLIENT_ID;
        try {
            if (!SessionSave.getSession("client_id", PayPalAccessTokenService.this).equals("")) {
                CONFIG_CLIENT_ID = SessionSave.getSession("client_id", PayPalAccessTokenService.this) + ":" + "EOupAFVBS1Y_wujXI67ss7FaZJXID0RMsi8ZIYmlNsqfdV1DZdGH98zR4Xfj71l40n4gsoBKCicpoyJJ";
            } else {
                CONFIG_CLIENT_ID = "AfiSFrZ9FxvKNGLpSPs-r4d0cD7_NpMKl3sjmhzTYgn5x-wlFX4Behtgbvizu5TWTrF8MfcN1k64Cn6r" + ":" + "EOupAFVBS1Y_wujXI67ss7FaZJXID0RMsi8ZIYmlNsqfdV1DZdGH98zR4Xfj71l40n4gsoBKCicpoyJJ";
            }
            byte[] data = CONFIG_CLIENT_ID.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
            url = new URL("https://api.sandbox.paypal.com/v1/oauth2/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", "Basic " + base64);
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write("grant_type=client_credentials");

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }

            System.out.println("response " + response);
            sendResultBroadcast(response);

        } catch (IOException e) {
// TODO Auto-generated catch block
        }


    }

    void sendResultBroadcast(String jsonResult) {

        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            String token_id = jsonObject.getString("access_token");

            System.out.println("response token_id " + token_id);

            Log.d("sendResultBroadcast", "sendResultBroadcast: " + jsonResult);
            SessionSave.saveSession("token_id", token_id, PayPalAccessTokenService.this);
            Intent intent = new Intent(PayPalAccessTokenService.this, PayPayUserInfoService.class);
            startService(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
