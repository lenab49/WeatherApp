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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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
    TextView txtdes ;
    TextView txtminT ;
    TextView txtmaxT ;
    ImageView imgicon;
    TextView txtsunset ;
    TextView txtsunrise ;

    TextView tomortxtmin;
    TextView tomortxtmax;
    TextView secnddaytxtmin;
    TextView secnddaytxtmax;
    TextView thirdtxtmin;
    TextView thirdtxtmax;
    TextView fourthmin;
    TextView fourthmax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);
        startDownload4days(Ville);
         txtdes = (TextView) findViewById(R.id.textDescription);
         txtminT = (TextView) findViewById(R.id.textTempMin);
         txtmaxT = (TextView) findViewById(R.id.textTempMax);
         imgicon = (ImageView) findViewById(R.id.imgicon);
         txtsunset = (TextView) findViewById(R.id.textSunSet);
         txtsunrise = (TextView) findViewById(R.id.textSunRise);

         tomortxtmin=(TextView)findViewById(R.id.tomorMin);
         tomortxtmax=(TextView)findViewById(R.id.tomorMax);
         secnddaytxtmin=(TextView)findViewById(R.id.txt2ndMin);
         secnddaytxtmax=(TextView)findViewById(R.id.txt2ndMax);
         thirdtxtmin=(TextView)findViewById(R.id.txt3rdMin);
         thirdtxtmax=(TextView)findViewById(R.id.txt3rdMax);
         fourthmin=(TextView)findViewById(R.id.txt4thMin);
         fourthmax=(TextView)findViewById(R.id.txt4thMax);


        Bundle b = getIntent().getExtras();
        setValue(-1); // or other values
        if (b != null) {
            setValue(b.getInt("key"));
            Singleton o = Singleton.getInstance();
            weather = o.getData(getValue());
            setTitle(weather.getVille());
            Ville=(weather.getVille());
            txtdes.setText(weather.getDescription());
            txtminT.setText("Min T°"+Double.toString(weather.getMinTemp()));
            txtmaxT.setText("Max T°"+Double.toString(weather.getMaxTemp()));
           // Date d=new Date(Long.parseLong(Long.toString(weather.getSunrise())) * 1000L);
            //String h=Long.toString(d.getTime());
            //txtsunrise.setText(h);
            txtsunrise.setText(getString(R.string.txtsunrise)+" "+ConvertTimestamp(weather.getSunrise()));
            txtsunset.setText(getString(R.string.txtsunset)+" "+ConvertTimestamp(weather.getSunset()));
            String icon = weather.getIcon();
            Log.e(TAG,"icon est "+icon);
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
                case "04d":
                    imgicon.setImageResource(R.drawable.big_04d);
                    break;
                case "04n":
                    imgicon.setImageResource(R.drawable.big_04n);
                    break;
                case "09d":
                    imgicon.setImageResource(R.drawable.big_09d);
                    break;
                case "09n":
                    imgicon.setImageResource(R.drawable.big_09n);
                    break;
                case "10d":
                    imgicon.setImageResource(R.drawable.big_10d);
                    break;
                case "10n":
                    imgicon.setImageResource(R.drawable.big_10n);
                    break;
                case "11d":
                    imgicon.setImageResource(R.drawable.big_11d);
                    break;
                case "11n":
                    imgicon.setImageResource(R.drawable.big_11n);
                    break;
                case "13d":
                    imgicon.setImageResource(R.drawable.big_13d);
                    break;
                case "13n":
                    imgicon.setImageResource(R.drawable.big_13n);
                    break;
                case "50d":
                    imgicon.setImageResource(R.drawable.big__50d);
                    break;
                case "50n":
                    imgicon.setImageResource(R.drawable.big__50n);
                    break;
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void Refresh (){

        Singleton o = Singleton.getInstance();
        Weather weather = o.getData(getValue());
        setTitle(weather.getVille());
        Ville=(weather.getVille());
        txtdes.setText(weather.getDescription());
        txtminT.setText(Double.toString(weather.getMinTemp()));
        txtmaxT.setText(Double.toString(weather.getMaxTemp()));

        startDownload4days(weather.getVille());

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
           Log.w(TAG,"List parse ="+listwparse.size());

        }

        if (listwparse != null) {
           displayWeatherInformation4days(listwparse);

            //CreatetxtFile(response);

        }

        // Toast.makeText(this, "Response received.", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Response received");
       /* mStartDownloadButton.setEnabled(true);
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoadingTextView.setVisibility(View.INVISIBLE);
        */
    }
    private void displayWeatherInformation4days(ArrayList<Weather> wr) {
        int i=0;

        for(i=0;i<wr.size();i++){

            if(i==0) {
                tomortxtmin.setText(Double.toString(wr.get(i).getMinTemp()));
                tomortxtmax.setText(Double.toString(wr.get(i).getMaxTemp()));
            }
            else{if(i==1){
                    secnddaytxtmin.setText(Double.toString(wr.get(i).getMinTemp()));
                    secnddaytxtmax.setText(Double.toString(wr.get(i).getMaxTemp()));
                }
                else{if(i==2){
                    thirdtxtmin.setText(Double.toString(wr.get(i).getMinTemp()));
                    thirdtxtmax.setText(Double.toString(wr.get(i).getMaxTemp()));
                        }
                        else {
                            if (i == 3) {
                            fourthmin.setText(Double.toString(wr.get(i).getMinTemp()));
                            fourthmax.setText(Double.toString(wr.get(i).getMaxTemp()));

                            }
                       }
            }
                }

            }

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_refresh:
                Log.e(TAG,"Refresh");
                Refresh();
                return true;
            case R.id.action_delete:
                deleteCity();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
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
