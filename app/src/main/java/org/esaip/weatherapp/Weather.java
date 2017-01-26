package org.esaip.weatherapp;

/**
 * Custom data holder for weather information
 */

public class Weather {

    private double latitude;
    private double longitude;
    private double currentTemp;
    private double maxTemp;
    private double minTemp;
    private long sunset;
    private long sunrise;
    private String description;
    private String ville;
    private String icon;



    private String date;



    public Weather(double latitude, double longitude,long sunset,long sunrise, double currentTemp,
                   double maxTemp, double minTemp, String description, String ville,String icon,String date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunset=sunset;
        this.sunrise=sunrise;
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.description = description;
        this.ville=ville;
        this.icon=icon;
        this.date=date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getDescription() {
        return description;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getSummary() {
        String info = "Weather Info:\nCurrent temp: " + currentTemp +
                "°C\nCurrent weather: " + description;
        return info;
    }
}
