package org.esaip.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class AddCity extends AppCompatActivity {

    private TextView mCityText;
    private Button mValidCityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        mCityText = (TextView) findViewById(R.id.CityText) ;
        mValidCityButton = (Button) findViewById(R.id.ValidCityButton);

        mValidCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = mCityText.getText().toString();
                if (city.length() < 1){
                    Toast.makeText(AddCity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent=new Intent();
                    intent.putExtra("city",city);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }


}
