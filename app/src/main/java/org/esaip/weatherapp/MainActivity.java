package org.esaip.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.esaip.weatherapp.R.id.GdCity;

public class MainActivity extends AppCompatActivity {
    private GridView GdCit;
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String YOUR_API_KEY = "077a245d87c02f81dc701f309926ef48";

    private static final String JSON_FORMAT = "json";
    private final String formatUsed = JSON_FORMAT;

    private MyAsyncTask Task;

    private int i = 0;
    private int p = 0;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public ArrayList<Weather> listcity = new ArrayList<>();
    private Weather weather;
    private ArrayList<Weather> slistcity = new ArrayList<>();
    private String Ville = null;

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    private boolean refresh;

    private static final String VALUES = "org.esaip.myfirstapplication.data.VALUES";
    private static int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GdCit = (GridView) findViewById(GdCity);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCitybyMapsActivity.class);
                startActivityForResult(intent, requestCode);
            }
        });
        GdCit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int d=0;d<listcity.size();d++){
                    Singleton singleton=Singleton.getInstance();
                    singleton.setData(listcity.get(d));
                }
                Intent intent = new Intent(MainActivity.this, DetailWeather.class);
                Bundle b = new Bundle();
                b.putInt("key", position);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }

            ;

        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultcode, Intent data) {
        super.onActivityResult(requestCode, resultcode, data);
        if (requestCode == 1) {
            String whatYouSent = data.getStringExtra("city");
            setRefresh(false);
            startDownload(whatYouSent);
        }
        if(requestCode == 2){
            String retour = data.getStringExtra("delete");
            int longueur = listcity.size();
            for (int i=0; i< listcity.size(); i++){
                Weather villeEnCours = slistcity.get(i);
                if (weather.getVille() == retour){
                    slistcity.remove(i);
                    listcity.remove(i);
                    i = longueur;
                }
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        saveData();

    }

    @Override
    protected void onResume() {
        //onResume permet de retourner a l'ecran et d'ppliquer les préférences au démarrage et lorsqu'on redemarre activité
        //on va retourner les données sauvegardées
        super.onResume();
        loadData();
        if(listcity.isEmpty()){
            for(int d=0;d<slistcity.size();d++){
                listcity.add(slistcity.get(d));

            }
        }
    }


    private URL buildURL(String frenchCity) {
        final String BASE_URL =
                "http://api.openweathermap.org/data/2.5/weather?";
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

    public void startDownload(String city) {
        if (isConnectionAvailable()) {
            Task = new MyAsyncTask(this);
            Task.execute(buildURL(city));
        } else {
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
        if (formatUsed.equals(JSON_FORMAT)) {
            weather = parseJsonData(response);
        }
        if (weather != null) {
            displayWeatherInformation(weather);
        }
        Log.e(TAG, "Response received");
    }

    private void displayWeatherInformation(Weather weather) {

        if (isRefresh() == true) {
            listcity.set(getI(), weather);
            DataCityAdapter dataCityAdapter = new DataCityAdapter(this, listcity);
            GdCit.setAdapter(dataCityAdapter);
        } else {
            listcity.add(getI(), weather);
            DataCityAdapter dataCityAdapter = new DataCityAdapter(this, listcity);
            GdCit.setAdapter(dataCityAdapter);
            setI(getI() + 1);
        }
    }

    private Weather parseJsonData(String jsonFeed) {
        JsonParser parser = new JsonParser();
        return parser.parse(jsonFeed);
    }
    private Weather parseSaveJsonData(JSONObject jobj){
        JsonParser parser=new JsonParser();
        return parser.SaveWeather(jobj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    private void refreshData() {
        setRefresh(true);

        if (listcity.size() != 0) {
            for (p = 0; p < listcity.size(); p++) {
                setI(p);
                String Ville = (listcity.get(p)).getVille();
                startDownload(Ville);
            }

        }
    }

    private void clear() {
        setI(0);
        listcity.clear();
        slistcity.clear();
        DataCityAdapter dataCityAdapter = new DataCityAdapter(this, listcity);
        GdCit.setAdapter(dataCityAdapter);
    }
   // sauvegarder les données de l'application

    private void saveData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listcity); // myObject - instance of MyObject
       // Log.e(TAG,"JSON sav ="+json);
        editor.putString("MyObject", json);
        editor.putInt("index", i);
        editor.commit();
    }
    //Chargement des données sauvegardées
    private void loadData() {
        Weather weather = null;
        SharedPreferences defaultSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = defaultSharedPref.getString("MyObject", null);
        try {

            JSONArray obj = new JSONArray(json);
            int taille = obj.length();
            for (int m = 0; m < taille; m++) {
                weather = parseSaveJsonData(obj.getJSONObject(m));
                slistcity.add(weather);
            }
            DataCityAdapter dataCityAdapter = new DataCityAdapter(this, slistcity);
            GdCit.setAdapter(dataCityAdapter);
        } catch (Exception e) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
        }
        setI(defaultSharedPref.getInt("index", 0));

    }

}
