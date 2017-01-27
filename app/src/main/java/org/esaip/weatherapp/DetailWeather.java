package org.esaip.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class DetailWeather extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String JSON_FORMAT = "json";
    private final String formatUsed = JSON_FORMAT;
    private static final String YOUR_API_KEY = "077a245d87c02f81dc701f309926ef48";
    private String Ville=null;
    private Weather weather;
    private MyAsyncTask Task;
    private ShareActionProvider mShareActionProvider;
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

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
        setValue(-1); // or other values
        if (b != null) {
            setValue(b.getInt("key"));
            Singleton o = Singleton.getInstance();
            Weather weather = o.getData(getValue());
            setTitle(weather.getVille());
            Ville=(weather.getVille());
            txtdes.setText(weather.getDescription());
            txtminT.setText(Double.toString(weather.getMinTemp()));
            txtmaxT.setText(Double.toString(weather.getMaxTemp()));
           // Date d=new Date(Long.parseLong(Long.toString(weather.getSunrise())) * 1000L);
            //String h=Long.toString(d.getTime());
            //txtsunrise.setText(h);
            txtsunrise.setText(getString(R.string.txtsunrise)+" "+ConvertTimestamp(weather.getSunrise()));
            txtsunset.setText(getString(R.string.txtsunset)+" "+ConvertTimestamp(weather.getSunset()));
            String icon = weather.getIcon();
            switch (icon) {
                case "01d":
                    imgicon.setImageResource(R.drawable.big_01d);
                    break;
                case "01n":
                    imgicon.setImageResource(R.drawable.big_01n);
                    break;
                case "02n":
                    imgicon.setImageResource(R.drawable.big_02n);
                    break;
                case "02d":
                    imgicon.setImageResource(R.drawable.big_02d);
                    break;
                case "03d":
                    imgicon.setImageResource(R.drawable.big_03d);
                    break;
                case "03n":
                    imgicon.setImageResource(R.drawable.big_03n);
                    break;
                case "big_04d":
                    imgicon.setImageResource(R.drawable.bro_clouds);
                    break;
                case "big_04n":
                    imgicon.setImageResource(R.drawable.nbro_clouds);
                    break;
                case "big_09d":
                    imgicon.setImageResource(R.drawable.show_rain);
                    break;
                case "big09n":
                    imgicon.setImageResource(R.drawable.nshow_rain);
                    break;
                case "big_10d":
                    imgicon.setImageResource(R.drawable.rain);
                    break;
                case "big_10n":
                    imgicon.setImageResource(R.drawable.nrain);
                    break;
                case "big_11d":
                    imgicon.setImageResource(R.drawable.thunderstorm);
                    break;
                case "big_11n":
                    imgicon.setImageResource(R.drawable.nthunderstorm);
                    break;
                case "big_13d":
                    imgicon.setImageResource(R.drawable.snow);
                    break;
                case "big_13n":
                    imgicon.setImageResource(R.drawable.nsnow);
                    break;
                case "big_50d":
                    imgicon.setImageResource(R.drawable.mist);
                    break;
                case "big_50n":
                    imgicon.setImageResource(R.drawable.nmist);
                    break;
            }
            Log.v(TAG, "Value=" + Singleton.getInstance().getClass());
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public String ConvertTimestamp(Long lgvalue){
        long unixSeconds = lgvalue;
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);

        return formattedDate;
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

    public void startDownload4days(String city) {
        if (isConnectionAvailable()) {
            //  GdCit.setVisibility(View.INVISIBLE);
           Task = new MyAsyncTask(this);
            //toutes les méthodes vont être appelées
            Task.execute(buildURL4days(city));
        } else {
            Toast.makeText(this, "No connection available. Request canceled", Toast.LENGTH_SHORT).show();
        }
    }
    private URL buildURL4days(String frenchCity) {
        final String BASE_URL =
                "http://api.openweathermap.org/data/2.5/forecast/daily";
        final String QUERY_PARAM = "q";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String APP_ID = "APPID";
        final String LANG="lang";
        final String COUNT="cnt";

        String format = formatUsed; //Either "xml" or "json"
        String units = "metric";
        String lang=getString(R.string.lang);
        String days="4";
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(LANG,lang)
                .appendQueryParameter(QUERY_PARAM, frenchCity + ",fr")
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(COUNT,days)
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
    private ArrayList<Weather> parseJsonData(String jsonFeed) {
        JsonParser parser = new JsonParser();
        return parser.parse4Days(jsonFeed);
    }

    public void responseReceived(String response) {
        Log.i(TAG, "Response: " + response);
        ArrayList<Weather> listwparse=new ArrayList<>();
        //weather = null;

        if (formatUsed.equals(JSON_FORMAT)) {
            listwparse = parseJsonData(response);
            String ts=listwparse.get(0).getDate();
           Log.w(TAG,"Weather="+listwparse.get(0).getMinTemp());

        }

        if (weather != null) {
           displayWeatherInformation4days(weather);
            //CreatetxtFile(response);

        }

        // Toast.makeText(this, "Response received.", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Response received");
       /* mStartDownloadButton.setEnabled(true);
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoadingTextView.setVisibility(View.INVISIBLE);
        */
    }
    private void displayWeatherInformation4days(Weather w) {


        Log.d(TAG,"Ville "+w.getSummary());
        Log.d(TAG,"Min_temp"+Double.toString(w.getMinTemp()));

    }
        /*    listcity.add(getI(), weather);
            DataCityAdapter dataCityAdapter = new DataCityAdapter(this, listcity);
            GdCit.setAdapter(dataCityAdapter);
            setI(getI() + 1);
        */

        //Toast.makeText(this, "after dis i=" + getI(), Toast.LENGTH_SHORT).show();



    private boolean isConnectionAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Singleton sing=Singleton.getInstance();
        Weather weather = sing.getData(getValue());
        getMenuInflater().inflate(R.menu.menu_detailweather, menu);
        MenuItem shareItem=(MenuItem)menu.findItem(R.id.menu_item_share);
        ShareActionProvider mShare=(ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
      //shareIntent.putExtra(Intent.EXTRA_TEXT,Uri.parse("file://" + your_file_path)););
        String txt="API Méteo à "+weather.getVille()+"Il fait "+Double.toString(weather.getCurrentTemp())+"°C"+"Le temps est"+weather.getDescription();
        shareIntent.putExtra(Intent.EXTRA_TEXT,txt);
        mShare.setShareIntent(shareIntent);
        return true;
    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Inflate menu resource file.
        //getMenuInflater().inflate(R.menu.menu_detailweather, menu);
        //MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        //ShareActionProvider myShareActionProvider =
        //       (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        switch (item.getItemId()) {
            case R.id.menu_item_share:

                Intent myShareIntent = new Intent(Intent.ACTION_SEND);
                myShareIntent.setType("text/*");
                File f = new File("F:/ESAIP_2016-2017/Android/WeatherApp/file.txt");
                myShareIntent.putExtra(Intent.EXTRA_TEXT, Uri.fromFile(f));
                // myShareActionProvider.setShareIntent(myShareIntent);
                startActivity(Intent.createChooser(myShareIntent,"Share via"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void CreatetxtFile(String content) {
        String filename = "myfile";

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onBackPressed() {
        startActivity(new Intent(DetailWeather.this, MainActivity.class));
        finish();
    }

    public void deleteCity(){

        Intent result = new Intent();
        String cityDelete = weather.getVille();
        result.putExtra("delete", cityDelete);
        setResult(3, result);
        finish();
    }



}
