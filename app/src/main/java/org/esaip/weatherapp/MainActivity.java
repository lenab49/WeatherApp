package org.esaip.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.esaip.weatherapp.R.id.GdCity;

public class MainActivity extends AppCompatActivity {
    private GridView GdCit;
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String YOUR_API_KEY = "077a245d87c02f81dc701f309926ef48";


    private static final String JSON_FORMAT = "json";
    private final String formatUsed = JSON_FORMAT;
    private MyAsyncTask Task;
    private int i=0;
    private ArrayList<Weather> listcity=new ArrayList<>();
    private Weather weather;
    private ArrayList<Weather> slistcity=new ArrayList<>();
    private String Ville=null;

    private static int RESULT_CODE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GdCit=(GridView)findViewById(GdCity);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddCity.class);
                startActivityForResult(intent, RESULT_CODE);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String returnCity = data.getStringExtra("city");
        startDownload(returnCity);
    }

    @Override
    protected void onStop(){
        super.onStop();
        saveData();
    }
    @Override
    protected void onResume() {
        //onResume permet de retourner a l'ecran et d'ppliquer les préférences au démarrage et lorsqu'on redemarre activité
        super.onResume();
        if(loadData().size()!=0) {
            DataCityAdapter dataCityAdapter = new DataCityAdapter(this, loadData());
            GdCit.setAdapter(dataCityAdapter);
        }
    }

    private URL buildURL(String frenchCity) {
        final String BASE_URL =
                "http://api.openweathermap.org/data/2.5/weather?";
        final String QUERY_PARAM = "q";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String APP_ID = "APPID";

        String format = formatUsed; //Either "xml" or "json"
        String units = "metric";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
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

    public void startDownload(String city) {
        if(isConnectionAvailable()) {
          //  GdCit.setVisibility(View.INVISIBLE);
            Task = new MyAsyncTask(this);
            //toutes les méthodes vont être appelées
            Task.execute(buildURL(city));
        } else {
            Toast.makeText(this, "No connection available. Request canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnectionAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public void responseReceived(String response) {
        Log.i(TAG, "Response: " + response);

        weather = null;
        if(formatUsed.equals(JSON_FORMAT)) {
            weather = parseJsonData(response);

        }

        if(weather!=null) {
            displayWeatherInformation(weather);
        }

       // Toast.makeText(this, "Response received.", Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Response received");
       /* mStartDownloadButton.setEnabled(true);
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoadingTextView.setVisibility(View.INVISIBLE);
        */
    }

    private void displayWeatherInformation(Weather weather) {
       // GdCit.setText(weather.getSummary());
        //GdCit.set
      //  Toast.makeText(this,"Le resultat "+weather.getSummary(),Toast.LENGTH_SHORT).show();
        listcity.add(i, weather);
        i++;
        DataCityAdapter dataCityAdapter=new DataCityAdapter(this,listcity);
        GdCit.setAdapter(dataCityAdapter);
    }
    private Weather parseJsonData(String jsonFeed) {
        JsonParser parser = new JsonParser();
        return parser.parse(jsonFeed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
                case R.id.action_settings:
                    return true;
                case R.id.action_refresh:
                    refreshData();
                    return true;
            case R.id.action_clearall:
                clear();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
            }

    }
    private void refreshData(){


        DataCityAdapter dataCityAdapter=new DataCityAdapter(this,listcity);
        GdCit.setAdapter(dataCityAdapter);
        dataCityAdapter.clear();
        for(int p=0;p<listcity.size();p++){
           Ville=(listcity.get(p)).getVille();
            startDownload(Ville);
        }

    }
    private void clear(){
        listcity.clear();
        DataCityAdapter dataCityAdapter=new DataCityAdapter(this,listcity);
        GdCit.setAdapter(dataCityAdapter);
    }

    /*
    but : sauvegarder les données de l'application
     */

    private void saveData() {

        SharedPreferences sp=this.getSharedPreferences("Valuestore",Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listcity); // myObject - instance of MyObject
        prefsEditor.putString("MyObject", json);
        Toast.makeText(this,"Enregistement",Toast.LENGTH_SHORT).show();
        prefsEditor.commit();
    }
    private ArrayList<Weather> loadData(){
        SharedPreferences sp =getSharedPreferences("Valuestore",Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sp.getString("MyObject", "");
        Type type = new TypeToken<List<Weather>>(){}.getType();
        ArrayList<Weather> slistcity= gson.fromJson(json, type);
        listcity=slistcity;
        return slistcity;

    }
}
