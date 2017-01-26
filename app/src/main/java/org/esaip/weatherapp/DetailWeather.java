package org.esaip.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class DetailWeather extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String JSON_FORMAT = "json";
    private final String formatUsed = JSON_FORMAT;
    private static final String YOUR_API_KEY = "077a245d87c02f81dc701f309926ef48";
    private String Ville=null;
    private Weather weather;
    private MyAsyncTask Task;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);

        TextView txtdes = (TextView) findViewById(R.id.textDescription);
        TextView txtminT = (TextView) findViewById(R.id.textTempMin);
        TextView txtmaxT = (TextView) findViewById(R.id.textTempMax);
        ImageView imgicon = (ImageView) findViewById(R.id.imgicon);
        TextView txtsunset = (TextView) findViewById(R.id.textSunSet);
        TextView txtsunrise = (TextView) findViewById(R.id.textSunRise);
        Button btn5=(Button)findViewById(R.id.GET);

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if (b != null) {
            value = b.getInt("key");
            Singleton o = Singleton.getInstance();
            Weather weather = o.getData(value);
            setTitle(weather.getVille());
            Ville=(weather.getVille());
            txtdes.setText(weather.getDescription());
            txtminT.setText(Double.toString(weather.getMinTemp()));
            txtmaxT.setText(Double.toString(weather.getMaxTemp()));
            Date d=new Date(Long.parseLong(Long.toString(weather.getSunrise())) * 1000L);
            String h=Long.toString(d.getTime());
            txtsunrise.setText(h);
            txtsunset.setText(Long.toString(weather.getSunset()));
            String icon = weather.getIcon();
            switch (icon) {
                case "01d":
                    imgicon.setImageResource(R.drawable.clear_sky);
                    break;
                case "01n":
                    imgicon.setImageResource(R.drawable.nclearsky);
                    break;
                case "02d":
                    imgicon.setImageResource(R.drawable.few_clouds);
                    break;
                case "02n":
                    imgicon.setImageResource(R.drawable.nfew_clouds);
                    break;
                case "03d":
                    imgicon.setImageResource(R.drawable.scatt_clouds);
                    break;
                case "03n":
                    imgicon.setImageResource(R.drawable.nscatt_clouds);
                    break;
                case "04d":
                    imgicon.setImageResource(R.drawable.bro_clouds);
                    break;
                case "04n":
                    imgicon.setImageResource(R.drawable.nbro_clouds);
                    break;
                case "09d":
                    imgicon.setImageResource(R.drawable.show_rain);
                    break;
                case "09n":
                    imgicon.setImageResource(R.drawable.nshow_rain);
                    break;
                case "10d":
                    imgicon.setImageResource(R.drawable.rain);
                    break;
                case "10n":
                    imgicon.setImageResource(R.drawable.nrain);
                    break;
                case "11d":
                    imgicon.setImageResource(R.drawable.thunderstorm);
                    break;
                case "11n":
                    imgicon.setImageResource(R.drawable.nthunderstorm);
                    break;
                case "13d":
                    imgicon.setImageResource(R.drawable.snow);
                    break;
                case "13n":
                    imgicon.setImageResource(R.drawable.nsnow);
                    break;
                case "50d":
                    imgicon.setImageResource(R.drawable.mist);
                    break;
                case "50n":
                    imgicon.setImageResource(R.drawable.nmist);
                    break;
            }


            btn5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDownload5days(Ville);
                }
            });
            Log.v(TAG, "Value=" + Singleton.getInstance().getClass());
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DetailWeather Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    public void startDownload5days(String city) {
        if (isConnectionAvailable()) {
            //  GdCit.setVisibility(View.INVISIBLE);
           Task = new MyAsyncTask(this);
            //toutes les méthodes vont être appelées
            Task.execute(buildURL5days(city));
        } else {
            Toast.makeText(this, "No connection available. Request canceled", Toast.LENGTH_SHORT).show();
        }
    }
    private URL buildURL5days(String frenchCity) {
        final String BASE_URL =
                "http://api.openweathermap.org/data/2.5/forecast?";
        final String QUERY_PARAM = "q";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String APP_ID = "APPID";
        final String LANG="lang";

        String format = formatUsed; //Either "xml" or "json"
        String units = "metric";
        String lang=getString(R.string.lang);
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(LANG,lang)
                .appendQueryParameter(QUERY_PARAM, frenchCity + ",fr")
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(APP_ID, YOUR_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            Log.i(TAG, "Request URL: " + uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Malformed URL");
        }
        return url;
    }
    private Weather parseJsonData(String jsonFeed) {
        JsonParser parser = new JsonParser();
        return parser.parse5Days(jsonFeed);
    }

    public void responseReceived(String response) {
        Log.i(TAG, "Response: " + response);

        weather = null;
        if (formatUsed.equals(JSON_FORMAT)) {
            weather = parseJsonData(response);
            Log.w(TAG,"Weather="+weather.getVille());
        }

        if (weather != null) {
          //  displayWeatherInformation(weather);
        }

        // Toast.makeText(this, "Response received.", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Response received");
       /* mStartDownloadButton.setEnabled(true);
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoadingTextView.setVisibility(View.INVISIBLE);
        */
    }

    private boolean isConnectionAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
