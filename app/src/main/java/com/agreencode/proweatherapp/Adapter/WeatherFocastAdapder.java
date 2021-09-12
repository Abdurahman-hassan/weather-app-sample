package com.agreencode.proweatherapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agreencode.proweatherapp.Common.Common;
import com.agreencode.proweatherapp.Model.forecast.WeatherForcastCollector;
import com.agreencode.proweatherapp.Model.geographicCoordinates.WeatherResultCollector;
import com.agreencode.proweatherapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class WeatherFocastAdapder extends RecyclerView.Adapter<WeatherFocastAdapder.myViewHolder> {

    private Context context;
    private WeatherForcastCollector forcastList;
    public WeatherFocastAdapder(Context context, WeatherForcastCollector forcastList) {
        this.context = context;
        this.forcastList = forcastList;
    }

    public WeatherFocastAdapder() {

    }

    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weekly_items, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull myViewHolder holder, int position) {

        //LoadIcon
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(forcastList.getList().get(position).getWeather().get(0).getIcon())
                .append(".png").toString())
                .into(holder.image_card);

        holder.date_card.setText(new StringBuilder(Common.ConvertUnixToDateWithoutDay(forcastList.getList().get(position).getDt())).toString());
        holder.day_card.setText(new StringBuilder(Common.ConvertUnixToDayAndHour(forcastList.getList().get(position).getDt())).toString());
        holder.temp_card.setText(new StringBuilder(String.valueOf(forcastList.getList().get(position).getMain().getTemp())).toString());
        holder.min_temp_card.setText(new StringBuilder(String.valueOf(forcastList.getList().get(position).getMain().getTemp_min())).append("°C").toString());
        holder.max_temp_card.setText(new StringBuilder(String.valueOf(forcastList.getList().get(position).getMain().getTemp_max())).append("°C").toString());
    }

    @Override
    public int getItemCount() {
        if (this.forcastList != null) {
            return forcastList.getList().size();
        }
        return 0;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView min_temp_card, max_temp_card, temp_card , date_card,day_card;
        ImageView image_card;

        public myViewHolder(View itemView) {
            super(itemView);

            min_temp_card = itemView.findViewById(R.id.min_temp_card);
            max_temp_card = itemView.findViewById(R.id.max_temp_card);
            temp_card = itemView.findViewById(R.id.temp_card);
            date_card = itemView.findViewById(R.id.dateCard);
            day_card = itemView.findViewById(R.id.day_card);
            image_card= itemView.findViewById(R.id.img_Card);

        }
    }
}
