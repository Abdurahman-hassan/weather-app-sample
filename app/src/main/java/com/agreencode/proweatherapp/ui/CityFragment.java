package com.agreencode.proweatherapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class CityFragment extends Fragment {

    private List<String>listCities;
    private MaterialSearchBar searchBar;
    TextView city, coun, des, feel, tem, dat, hum, win, pre;
    ImageView img;
    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;
    static CityFragment instance;


    public static CityFragment getInstance() {
        if (instance == null)
            instance = new CityFragment();
        return instance;
    }

    public CityFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClint.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_city, container, false);

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
        searchBar = itemView.findViewById(R.id.search_bar);
        searchBar.setEnabled(false);

        //TODO: add more that one city

        new LoadCities().execute();

        return itemView;
    }

    private class LoadCities extends SimpleAsyncTask<List<String>> {
        @Override
        protected List<String> doInBackground() {
            listCities = new ArrayList<>();
            try {
                StringBuilder builder = new StringBuilder();
                InputStream inputStream = getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
                InputStreamReader reader = new InputStreamReader(gzipInputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String hasReaded;
                while ((hasReaded = bufferedReader.readLine()) != null){

                    builder.append(hasReaded);
                    listCities = new Gson().fromJson(builder.toString(),new TypeToken<List<String>>(){}.getType());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return listCities;
        }

        @Override
        protected void onSuccess(final List<String> Citylistt) {
            super.onSuccess(Citylistt);

            searchBar.setEnabled(true);
            searchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    List<String> suggest = new ArrayList<>();
                    for (String search:Citylistt) {

                        if(search.toLowerCase().contains(searchBar.getText().toLowerCase())){
                            suggest.add(search);
                        }
                    }
                    searchBar.setLastSuggestions(suggest);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {

                }

                @Override
                public void onSearchConfirmed(CharSequence text) {

                    getWeatherInformation(text.toString());
                    searchBar.setLastSuggestions(Citylistt);
                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });

           searchBar.setLastSuggestions(Citylistt);

        }
    }

    private void getWeatherInformation(String cityName) {
        compositeDisposable.add(mService.geographicCoordinatesByCityName(
                cityName,
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


