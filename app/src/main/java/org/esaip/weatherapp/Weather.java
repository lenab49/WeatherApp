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



    public Weather(double latitude, double longitude, double currentTemp,
                   double maxTemp, double minTemp, String description, String ville) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;

        this.description = description;
        this.ville=ville;
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

    public String getDescription() {
        return description;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getSummary() {
        String info = "Weather Info:\nCurrent temp: " + currentTemp +
                "°C\nCurrent weather: " + description;
        return info;
    }
}
