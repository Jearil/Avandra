package com.linkedin.avandra.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Track things the user does.
 */
public class MetricsIntentService extends IntentService {

    public static final String TAG = "MetricsIntentService";
    public static final String LIST_TAP = "ListTap";
    public static final String SERVER = "Server";

    public MetricsIntentService() {
        super("MetricsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String metric = intent.getStringExtra(TAG);
            int increment = 1;
            if (SERVER.equals(metric)) {
                increment = getServerIncrement();
            }
            incrementMetric(metric, increment);
        }
    }

    private int getServerIncrement() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(new HttpGet("http://localhost:1234"));

            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                return Integer.parseInt(responseString);
            }

        } catch (IOException ioe) {
            Log.e(TAG, "Was unable to make HTTP request");
        } catch (NumberFormatException nfe) {
            Log.e(TAG, "The server did not return a number");
        }

        return 1;
    }

    private void incrementMetric(String metric, int increment) {
        SharedPreferences preferences = getSharedPreferences(TAG, Context.MODE_PRIVATE);
        preferences.edit().putInt(metric, preferences.getInt(metric, 0) + increment).commit();
    }
}
