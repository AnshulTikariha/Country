package com.project.countries.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.project.countries.Database.RoomDB;
import com.project.countries.Model.Country;
import com.project.countries.R;

public class CountryAdapter extends  RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    private Context context;
    Country[] models;
    RoomDB database;

    public CountryAdapter(Context context, Country[] apiModel){
        this.context=context;
        this.models =apiModel;
    }


    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_items_layout,parent,false);
        return new CountryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CountryViewHolder holder, final int position) {
        final Country List= models[position];

     //   System.out.println("DEBUG" + List.getLanguages());

        database = RoomDB.getInstance(context);


        holder.country_name.setText(List.getName());
        holder.border.setText(List.getBorders().toString());
        holder.capital.setText(List.getCapital());
        holder.Region.setText(List.getRegion());
        holder.SubRegion.setText(List.getSubregion());
        holder.Population.setText(List.getPopulation() + "");

        SvgLoader.pluck()
                .with((Activity) context)
                .load(List.getFlag(), holder.flag);






    }

    @Override
    public int getItemCount() {
        try{return models.length;}
        catch (Exception e){

        }
        return 0;
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        TextView country_name,capital,Region,SubRegion,Population,border;
        ImageView flag;

        public CountryViewHolder(View itemView) {
            super(itemView);

            country_name= itemView.findViewById(R.id.country_name);
            capital = itemView.findViewById(R.id.country_capital);
            Region = itemView.findViewById(R.id.country_region);
            SubRegion = itemView.findViewById(R.id.country_subregion);
            Population = itemView.findViewById(R.id.country_population);
            border= itemView.findViewById(R.id.border);
            flag = itemView.findViewById(R.id.flag);

        }


    }
}

