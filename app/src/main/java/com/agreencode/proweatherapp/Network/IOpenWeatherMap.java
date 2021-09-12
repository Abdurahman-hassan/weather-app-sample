package com.agreencode.proweatherapp.Network;

import com.agreencode.proweatherapp.Model.forecast.WeatherForcastCollector;
import com.agreencode.proweatherapp.Model.geographicCoordinates.WeatherResultCollector;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap { //
    @GET("weather")
    Observable<WeatherResultCollector> geographicCoordinatesByLatLan(@Query("lat") String lat,
                                                          @Query("lon") String lng,
                                                          @Query("appid") String app_id,
                                                          @Query("units") String unit);


    @GET("forecast")
    Observable<WeatherForcastCollector> byWeatherForecast(@Query("lat") String lat,
                                                          @Query("lon") String lng,
                                                          @Query("appid") String app_id,
                                                          @Query("units") String unit);

    @GET("weather")
    Observable<WeatherResultCollector> geographicCoordinatesByCityName(@Query("q") String cityName,
                                                                        @Query("appid") String app_id,
                                                                        @Query("units") String unit);
}
