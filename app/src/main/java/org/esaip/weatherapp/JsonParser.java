package org.esaip.weatherapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            String date=null;


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




            weather = new Weather(latitude, longitude,sunset,sunrise, currentTemp, maxTemp, minTemp, description,ville,icon,date);

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
        String date=null;

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




            weather = new Weather(latitude, longitude,sunset,sunrise, currentTemp, maxTemp, minTemp, description,ville,icon,date);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return weather;
        }
    public Weather parse5Days(String txtjson){
        Weather weather=null;
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
            String date=null;

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
                    if (temp.has("min")) {
                        minTemp = temp.getDouble("temp_min");
                        Log.d(JsonParser.class.getSimpleName(),"min: "+ minTemp);
                    }
                    if(temp.has("max")){
                        maxTemp = temp.getDouble("temp_max");
                        Log.d(JsonParser.class.getSimpleName(),"max: "+ maxTemp);
                    }
                }
                if(list.has("dt_txt")){
                    JSONObject listObject = list.getJSONObject("dt_txt");
                    date=listObject.getString("dt_text");
                    Log.d(JsonParser.class.getSimpleName(),"dt_txt: "+ date);
                }
            }

            if(root.has("weather")){
                JSONArray weatherArray = root.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                icon = weatherObject.getString("icon");
            }
            if(root.has("list")){

                JSONArray list = root.getJSONArray("list");
                JSONObject listObject = list.getJSONObject(0);
                date=listObject.getString("dt_text");

            }

            weather = new Weather(latitude, longitude,sunset,sunrise, currentTemp, maxTemp, minTemp,description,ville,icon,date);
            Log.e("Retour",weather.getVille());

        }
            catch (JSONException e){
                e.printStackTrace();
            }
            return weather;

    }
}
