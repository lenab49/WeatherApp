package org.esaip.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
            TextView curtemp=(TextView)view.findViewById(R.id.list_itemWeather_curtemp);
            curtemp.setText(listWeather.get(pos).getVille());
            TextView description=(TextView)view.findViewById(R.id.list_itemWeather_descr);
            description.setText(listWeather.get(pos).getDescription());
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
