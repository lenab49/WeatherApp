package org.esaip.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Léna on 24/01/2017.
 */

public class TestDifferentVille extends AppCompatActivity{
    private static final String VALUES = "org.esaip.myfirstapplication.data.VALUES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_differentville);
        EditText txt=(EditText)findViewById(R.id.txtville);
        Button btn=(Button)findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText txt=(EditText)findViewById(R.id.txtville);
                if (txt.getText().length()<1){
                    txt.setError("Please write something");
                }
                else {
                    Intent result = new Intent();
                    result.putExtra("city", txt.getText().toString());
                    setResult(2, result);
                    finish();
                }
            }
        });

       /* if (txt!=null){
            Intent sendStuff = new Intent(this, MainActivity.class);
            sendStuff.putExtra(VALUES, txt.getText());
            startActivity(sendStuff);
        }
        */
    }
}
