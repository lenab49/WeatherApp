package org.esaip.weatherapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Léna on 16/12/2016.
 */

public class JsonParser {

    public Weather parse(String feed) {
        Weather weather = null;

        try {
            double latitude = 0.0;
            double longitude = 0.0;
            double currentTemp = 0.0;
            double maxTemp = 0.0;
            double minTemp = 0.0;
            long sunset=0;
            long sunrise=0;
            String description = null;
            String ville=null;
            String icon=null;



            JSONObject root = new JSONObject(feed);

            if(root.has("coord")) {
                JSONObject coord = root.getJSONObject("coord");

                longitude = coord.getDouble("lon");
                latitude = coord.getDouble("lat");
            }
            if(root.has("sys")){
                JSONObject sys = root.getJSONObject("sys");
                sunset=sys.getLong("sunset");
                sunrise=sys.getLong("sunrise");
            }

            if(root.has("weather")) {
                JSONArray weatherArray = root.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                description = weatherObject.getString("description");
                icon=weatherObject.getString("icon");
            }

            if(root.has("main")) {
                JSONObject main = root.getJSONObject("main");

                currentTemp = main.getDouble("temp");
                maxTemp = main.getDouble("temp_min");
                minTemp = main.getDouble("temp_max");

            }
            if(root.has("name")){
                ville=root.getString("name");
            }




            weather = new Weather(latitude, longitude,sunset,sunrise, currentTemp, maxTemp, minTemp, description,ville,icon);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weather;
    }
    public Weather SaveWeather(JSONObject jobj){
        Weather weather=null;
        double latitude = 0.0;
        double longitude = 0.0;
        double currentTemp = 0.0;
        long sunset=0;
        long sunrise=0;
        double maxTemp = 0.0;
        double minTemp = 0.0;
        String description = null;
        String ville=null;
        String icon=null;

        try{
            latitude = jobj.getDouble("latitude");
            longitude = jobj.getDouble("latitude");
            sunset=jobj.getLong("sunset");
            sunrise=jobj.getLong("sunrise");
            currentTemp=jobj.getDouble("currentTemp");
            maxTemp=jobj.getDouble("maxTemp");
            minTemp=jobj.getDouble("minTemp");
            description=jobj.getString("description");
            ville=jobj.getString("ville");
            icon=jobj.getString("icon");




            weather = new Weather(latitude, longitude,sunset,sunrise, currentTemp, maxTemp, minTemp, description,ville,icon);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return weather;
        }
    public ArrayList<Weather> parse4Days(String txtjson){
        Weather weather=null;
        ArrayList<Weather> listparsew=new ArrayList<>();
        try {
            double latitude = 0.0;
            double longitude = 0.0;
            double currentTemp = 0.0;
            double maxTemp = 0.0;
            double minTemp = 0.0;
            long sunset = 0;
            long sunrise = 0;
            String description = null;
            String ville = null;
            String icon = null;


            JSONObject root = new JSONObject(txtjson);

            if (root.has("city")) {
                JSONObject city = root.getJSONObject("city");
                ville = city.getString("name");
            }

            JSONArray lists = root.getJSONArray("list");

            for (int i =0; i<lists.length();i++) {
                JSONObject list = lists.getJSONObject(i);
                if (list.has("temp")) {
                    JSONObject temp = list.getJSONObject("temp");
                        minTemp = temp.getDouble("min");
                        maxTemp = temp.getDouble("max");
                        Log.d(JsonParser.class.getSimpleName(),"max: "+ maxTemp);
                    }

                if(list.has("weather")){
                    JSONArray weatherArray = list.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    icon = weatherObject.getString("icon");
                }
                Log.d("Parse","="+minTemp);
                weather = new Weather(latitude, longitude,sunset,sunrise, currentTemp, maxTemp, minTemp,description,ville,icon);
                listparsew.add(i,weather);

            }
        }
            catch (JSONException e){
                e.printStackTrace();
            }
            return listparsew;

    }
}
