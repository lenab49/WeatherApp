package org.esaip.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Léna on 28/12/2016.
 */

public class DataCityAdapter extends ArrayAdapter<Weather> {
    private ArrayList<Weather> listWeather=new ArrayList<>();
    private Context context;
   public DataCityAdapter(Context context,ArrayList<Weather>  lWeather){
      super(context,-1,lWeather);
        this.context=context;
     listWeather=new ArrayList<Weather>(lWeather);
   }
    @Override
    public View getView(int pos, View convertView, ViewGroup parent){


        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if(convertView==null){
            view=new View(context);
            view=layoutInflater.inflate(R.layout.griditem_weatherdata,null);
            TextView cityn=(TextView)view.findViewById(R.id.list_itemWeather_city);
            cityn.setText(listWeather.get(pos).getVille());
            TextView description=(TextView)view.findViewById(R.id.list_itemWeather_descr);
            description.setText(listWeather.get(pos).getDescription());
            TextView currtemp=(TextView)view.findViewById(R.id.list_itemWeather_currenttemp);
            currtemp.setText(Double.toString(listWeather.get(pos).getCurrentTemp())+"°C");

            ImageView logow=(ImageView)view.findViewById(R.id.list_itemWeather_img);
            String icon=listWeather.get(pos).getIcon();
            switch(icon){
                case "01d":
                    logow.setImageResource(R.drawable.clear_sky);
                    break;
                case "01n":
                    logow.setImageResource(R.drawable.nclearsky);
                    break;
                case "02d":
                    logow.setImageResource(R.drawable.few_clouds);
                    break;
                case "02n":
                    logow.setImageResource(R.drawable.nfew_clouds);
                    break;
                case "03d":
                    logow.setImageResource(R.drawable.scatt_clouds);
                    break;
                case "03n":
                    logow.setImageResource(R.drawable.nscatt_clouds);
                    break;
                case "04d":
                    logow.setImageResource(R.drawable.bro_clouds);
                    break;
                case "04n":
                    logow.setImageResource(R.drawable.nbro_clouds);
                    break;
                case "09d":
                    logow.setImageResource(R.drawable.show_rain);
                    break;
                case "09n":
                    logow.setImageResource(R.drawable.nshow_rain);
                    break;
                case "10d":
                    logow.setImageResource(R.drawable.rain);
                    break;
                case "10n":
                    logow.setImageResource(R.drawable.nrain);
                    break;
                case "11d":
                    logow.setImageResource(R.drawable.thunderstorm);
                    break;
                case "11n":
                    logow.setImageResource(R.drawable.nthunderstorm);
                    break;
                case "13d":
                    logow.setImageResource(R.drawable.snow);
                    break;
                case "13n":
                    logow.setImageResource(R.drawable.nsnow);
                    break;
                case "50d":
                    logow.setImageResource(R.drawable.mist);
                    break;
                case "50n":
                    logow.setImageResource(R.drawable.nmist);
                    break;
            }

        }else{
            view=convertView;
        }


        return view;

    }
    @Override
    public Weather getItem(int position) {
        return null;
    }

    @Override
    public int getCount(){return listWeather.size();
    }
    @Override
    public long getItemId(int position){
        return 0;
    }

}
