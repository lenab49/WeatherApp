package org.esaip.weatherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class DetailWeather extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);
        TextView txtville=(TextView)findViewById(R.id.textDay);

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null) {
            value = b.getInt("key");
            Singleton o = Singleton.getInstance();
            Weather weather=o.getData(value);
            txtville.setText(weather.getVille());
            Log.v(TAG,"Value="+Singleton.getInstance().getClass());
        }
    }
}
