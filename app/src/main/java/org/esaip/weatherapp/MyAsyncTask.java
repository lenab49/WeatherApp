package org.esaip.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Léna on 16/12/2016.
 */

public class MyAsyncTask extends AsyncTask<URL, Integer, String> {

    private static final String TAG = MyAsyncTask.class.getSimpleName();

    private WeakReference<MainActivity> mActivity;
    private WeakReference<DetailWeather>mDetail;

    public MyAsyncTask(MainActivity activity) {
        this.mActivity = new WeakReference<>(activity);
    }
    public MyAsyncTask(DetailWeather activity) {
        this.mDetail = new WeakReference<>(activity);
    }

   /* @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mActivity.get() != null) {
            //mActivity.get().setupProgressUI();
            Log.d(TAG,"AVANT ");
        }
    }*/
    @Override
    protected String doInBackground(URL... urls) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw XML or JSON response as a string.
        String responseString = null;

        try {
            URL url = urls[0];

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            responseString = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing the stream", e);
                }
            }
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            Log.e(TAG, "null result");
        } else {
            Log.d(TAG, "Download completed");
            if(mActivity.get() != null) {
               mActivity.get().responseReceived(result);
                //Log.d(TAG,"RESULTATTTTTTT");
            }
        }
    }

}
