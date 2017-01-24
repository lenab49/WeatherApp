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
    private String description;
    private String ville;
    private String icon;



    public Weather(double latitude, double longitude, double currentTemp,
                   double maxTemp, double minTemp, String description, String ville,String icon) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.description = description;
        this.ville=ville;
        this.icon=icon;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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

    public String getSummary() {
        String info = "Weather Info:\nCurrent temp: " + currentTemp +
                "°C\nCurrent weather: " + description;
        return info;
    }
}
