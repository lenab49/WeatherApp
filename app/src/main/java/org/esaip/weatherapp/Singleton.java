package org.esaip.weatherapp;

import java.util.ArrayList;

/**
 * Created by Léna on 25/01/2017.
 */

public class Singleton {

    private static Singleton ourInstance = new Singleton();

    public ArrayList<Weather>liscity=new ArrayList<>();

    public static Singleton getInstance() {
        if(null==ourInstance){
            ourInstance=new Singleton();
        }

        return ourInstance;
    }
    public void setData(Weather weather){
        this.liscity.add(weather);
    }
    public Weather getData(int i){
        return this.liscity.get(i);
    }
    public int sizeList(){
        return liscity.size();
    }
    public void ClearList(){
        this.liscity.clear();
    }


    private Singleton() {
    }
}


