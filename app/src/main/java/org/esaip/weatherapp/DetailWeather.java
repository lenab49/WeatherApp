package org.esaip.weatherapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;

public class DetailWeather extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
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

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if (b != null) {
            value = b.getInt("key");
            Singleton o = Singleton.getInstance();
            Weather weather = o.getData(value);
            setTitle(weather.getVille());
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
