package com.agreencode.proweatherapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agreencode.proweatherapp.Adapter.WeatherFocastAdapder;
import com.agreencode.proweatherapp.Common.Common;
import com.agreencode.proweatherapp.Model.forecast.WeatherForcastCollector;
import com.agreencode.proweatherapp.Network.IOpenWeatherMap;
import com.agreencode.proweatherapp.Network.RetrofitClint;
import com.agreencode.proweatherapp.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ForcastFragment extends Fragment {
    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;
    static ForcastFragment instance;

    TextView CountryForecastWaeak, cityForecastWaek;
    RecyclerView recyclerView;
    WeatherFocastAdapder weatherFocastAdapder;

    public static ForcastFragment getInstance() {
        if (instance == null)
            instance = new ForcastFragment();
        return instance;
    }

    public ForcastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClint.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_weekly, container, false);

        recyclerView = itemView.findViewById(R.id.recyclerView);
        CountryForecastWaeak = itemView.findViewById(R.id.CountryNameWeak);
        cityForecastWaek = itemView.findViewById(R.id.cityNameWeak);
        recyclerView.setAdapter(weatherFocastAdapder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        getForeCastWeatherInformation();

        return itemView;
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void getForeCastWeatherInformation() {

        compositeDisposable.add(mService.byWeatherForecast(
                String.valueOf(Common.CURRENT_LOCATION.getLatitude()),
                String.valueOf(Common.CURRENT_LOCATION.getLongitude()),
                Common.APP_ID,
                "metric")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<WeatherForcastCollector>() {
                        @Override
                        public void accept(WeatherForcastCollector weatherForcast) throws Exception {

                            displayForeCastWeather(weatherForcast);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d("ERROR", "" + throwable.getMessage());
                        }
                    }));
    }

    private void displayForeCastWeather(WeatherForcastCollector weatherForcast) {
        cityForecastWaek.setText(weatherForcast.getCity().getName());
        CountryForecastWaeak.setText(weatherForcast.getCity().getCountry());
        weatherFocastAdapder = new WeatherFocastAdapder(getContext(),weatherForcast);
        recyclerView.setAdapter(weatherFocastAdapder);
    }



    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }



}
