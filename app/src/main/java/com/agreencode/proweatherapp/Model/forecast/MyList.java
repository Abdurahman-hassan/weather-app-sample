package com.agreencode.proweatherapp.Model.forecast;

import com.agreencode.proweatherapp.Model.geographicCoordinates.Clouds;
import com.agreencode.proweatherapp.Model.geographicCoordinates.Main;
import com.agreencode.proweatherapp.Model.geographicCoordinates.Sys;
import com.agreencode.proweatherapp.Model.geographicCoordinates.Weather;
import com.agreencode.proweatherapp.Model.geographicCoordinates.Wind;

import java.util.List;

public class MyList {

    private int dt;
    private Main main;
    private List<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private int visibility;
    private int pop;
    private Sys sys;
    private String dt_txt;

    public MyList() {
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
