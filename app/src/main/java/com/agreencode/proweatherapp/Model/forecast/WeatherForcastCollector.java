package com.agreencode.proweatherapp.Model.forecast;


import java.util.List;

public class WeatherForcastCollector {
    private String cod;
    private int message;
    private int cnt;
    private List<MyList> list;
    private City city;

    public WeatherForcastCollector() {

    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<MyList> getList() {
        return list;
    }

    public void setList(List<MyList> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
