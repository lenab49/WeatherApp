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
import android.widget.Toast;

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
                Intent intent = new Intent(MainActivity.this, TestDifferentVille.class);
                startActivityForResult(intent, requestCode);
            }
        });
        GdCit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Click ! "+position);
                for(int d=0;d<listcity.size();d++){
                    Singleton singleton=Singleton.getInstance();
                    singleton.setData(listcity.get(d));
                }
                Intent intent = new Intent(MainActivity.this, DetailWeather.class);
                Bundle b = new Bundle();
                b.putInt("key", position);
                intent.putExtras(b); //Put your id to your next Intent
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
            //Toast.makeText(this,"Valeur recup="+whatYouSent,Toast.LENGTH_SHORT).show();
            setRefresh(false);
            startDownload(whatYouSent);

        } else {
            //Toast.makeText(this,"RequestCode="+requestCode,Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        //Toast.makeText(this,"SAVING ...",Toast.LENGTH_SHORT).show();
        saveData();

    }

    @Override
    protected void onResume() {
        //onResume permet de retourner a l'ecran et d'ppliquer les préférences au démarrage et lorsqu'on redemarre activité
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
            //  GdCit.setVisibility(View.INVISIBLE);
            Task = new MyAsyncTask(this);
            //toutes les méthodes vont être appelées
            Task.execute(buildURL(city));
        } else {
            //Toast.makeText(this, "No connection available. Request canceled", Toast.LENGTH_SHORT).show();
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

        // Toast.makeText(this, "Response received.", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Response received");
       /* mStartDownloadButton.setEnabled(true);
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoadingTextView.setVisibility(View.INVISIBLE);
        */
    }

    private void displayWeatherInformation(Weather weather) {
        //GdCit.setText(weather.getSummary());
        //GdCit.set
        //Toast.makeText(this,"Le resultat "+weather.getSummary(),Toast.LENGTH_SHORT).show();
        // listcity.add(i, weather);

        //Toast.makeText(this,"bool"+isRefresh(),Toast.LENGTH_SHORT).show();

        //for(p=0;p<listcity.size();p++) {
        //Toast.makeText(this, "Ville=" + listcity.get(p).getVille(), Toast.LENGTH_SHORT).show();
        //}
        if (isRefresh() == true) {
            // for (int d = 0; d <= getI(); d++) {

            listcity.set(getI(), weather);
            //  Toast.makeText(this, "d=" + d, Toast.LENGTH_SHORT).show();
            //}
            DataCityAdapter dataCityAdapter = new DataCityAdapter(this, listcity);
            GdCit.setAdapter(dataCityAdapter);


        } else {
            listcity.add(getI(), weather);
            DataCityAdapter dataCityAdapter = new DataCityAdapter(this, listcity);
            GdCit.setAdapter(dataCityAdapter);
            setI(getI() + 1);

        }
        Toast.makeText(this, "after dis i=" + getI(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(this, "SAving i= " + i, Toast.LENGTH_SHORT).show();

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
            //Toast.makeText(this,"size list="+listcity.size(),Toast.LENGTH_SHORT).show();
            for (p = 0; p < listcity.size(); p++) {
                setI(p);
                String Ville = (listcity.get(p)).getVille();
                startDownload(Ville);
                Toast.makeText(this, "Ville=" + listcity.get(p).getVille(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clear() {
        setI(0);
        listcity.clear();
        slistcity.clear();
        DataCityAdapter dataCityAdapter = new DataCityAdapter(this, listcity);
        //dataCityAdapter.clear();
        GdCit.setAdapter(dataCityAdapter);
    }

    /*
    but : sauvegarder les données de l'application
     */
    private void saveData() {
        Toast.makeText(this,"size ="+listcity.size(), Toast.LENGTH_SHORT).show();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listcity); // myObject - instance of MyObject
       // Log.e(TAG,"JSON sav ="+json);
        editor.putString("MyObject", json);
        editor.putInt("index", i);
        editor.commit();
    }

    private void loadData() {

        Weather weather = null;
        SharedPreferences defaultSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = defaultSharedPref.getString("MyObject", null);
        //  Log.e(TAG,"JSON ="+json);
        try {

            JSONArray obj = new JSONArray(json);
            int taille = obj.length();
            for (int m = 0; m < taille; m++) {

                weather = parseSaveJsonData(obj.getJSONObject(m));
                slistcity.add(weather);
                Log.d("Ville", weather.getVille());

            }
            DataCityAdapter dataCityAdapter = new DataCityAdapter(this, slistcity);
            GdCit.setAdapter(dataCityAdapter);

            Log.d(TAG, "Works" + slistcity.size());

        } catch (Exception e) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
        }
        setI(defaultSharedPref.getInt("index", 0));
        Log.d(TAG,"Valeur i= "+getI());

    }

}
