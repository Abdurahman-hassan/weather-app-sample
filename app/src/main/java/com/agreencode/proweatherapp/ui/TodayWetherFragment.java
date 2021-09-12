package com.agreencode.proweatherapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agreencode.proweatherapp.Common.Common;
import com.agreencode.proweatherapp.Model.geographicCoordinates.WeatherResultCollector;
import com.agreencode.proweatherapp.Network.IOpenWeatherMap;
import com.agreencode.proweatherapp.Network.RetrofitClint;
import com.agreencode.proweatherapp.R;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TodayWetherFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;
    static TodayWetherFragment instance;
    TextView city, coun, des, feel, tem, dat, hum, win, pre;
    ImageView img;

    public static TodayWetherFragment getInstance() {
        if (instance == null)
            instance = new TodayWetherFragment();

        return instance;
    }

    public TodayWetherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClint.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_day, container, false);

        city = itemView.findViewById(R.id.city_name);
        coun = itemView.findViewById(R.id.Country);
        img = itemView.findViewById(R.id.weather_resource_img);
        des = itemView.findViewById(R.id.describtion);
        feel = itemView.findViewById(R.id.temperature_feelsLike);
        tem = itemView.findViewById(R.id.temp_condition);
        dat = itemView.findViewById(R.id.date);
        hum = itemView.findViewById(R.id.humidity_value);
        win = itemView.findViewById(R.id.wind);
        pre = itemView.findViewById(R.id.pressure_value);

        getWeatherInformation();
        return itemView;
    }


    private void getWeatherInformation() {
        compositeDisposable.add(mService.geographicCoordinatesByLatLan(
                String.valueOf(Common.CURRENT_LOCATION.getLatitude()),
                String.valueOf(Common.CURRENT_LOCATION.getLongitude()),
                Common.APP_ID,
                "metric")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<WeatherResultCollector>() {
                            @Override
                            public void accept(WeatherResultCollector weatherResultCollector) throws Exception {

                                displayWeather(weatherResultCollector);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }));
    }
    private void displayWeather(WeatherResultCollector weatherResultCollector) {

        //Load image
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherResultCollector.getWeather().get(0).getIcon())
                .append(".png").toString())
                .into(img);

        // load information
        city.setText(new StringBuilder(weatherResultCollector.getName()).append(" ,").toString());
        des.setText(weatherResultCollector.getWeather().get(0).getDescription());
        dat.setText(Common.ConvertUnixToDate(weatherResultCollector.getDt()));
        tem.setText(new StringBuilder(String.valueOf(weatherResultCollector.getMain().getTemp())).append("°C").toString());
        feel.setText(new StringBuilder(String.valueOf(weatherResultCollector.getMain().getFeels_like())).append("°C").toString());
        hum.setText(new StringBuilder(String.valueOf(weatherResultCollector.getMain().getHumidity())).append("%").toString());
        win.setText(new StringBuilder(String.valueOf(weatherResultCollector.getWind().getSpeed())).append(" m/s W").toString());
        coun.setText(weatherResultCollector.getSys().getCountry());
        pre.setText(new StringBuilder(String.valueOf(weatherResultCollector.getMain().getPressure())).append(" hPa").toString());
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

}