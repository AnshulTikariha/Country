package com.project.countries;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.project.countries.Adapter.CountryAdapter;
import com.project.countries.Database.RoomDB;
import com.project.countries.Model.Country;
import com.project.countries.Model.Data;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String COUNTRY_URL = "https://restcountries.eu/rest/v2/region/asia";
    RecyclerView recyclerView;
    CountryAdapter countryAdapter;
    List<Data> dataList = new ArrayList<>();
    RoomDB database;
    LinearLayout offlineLayout,dataLayout;
    RelativeLayout noDataLayout;
    TextView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();

        recyclerView = findViewById(R.id.list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        offlineLayout = findViewById(R.id.offline_layout);
        offlineLayout.setVisibility(View.GONE);
        delete = findViewById(R.id.delete);
        dataLayout = findViewById(R.id.dataLayout);
        noDataLayout = findViewById(R.id.noDataLayout);
        dataLayout.setVisibility(View.VISIBLE);
        noDataLayout.setVisibility(View.GONE);

        try {
            if (database != null) {
                offlineLayout.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                Country[] models = gson.fromJson(String.valueOf(dataList.get(1).getText()), Country[].class);
                recyclerView.setAdapter(new CountryAdapter(getApplicationContext(), models));
                countryAdapter = new CountryAdapter(MainActivity.this, models);
                recyclerView.setAdapter(countryAdapter);

            } else {
                CheckConnection();
            }
        }catch (Exception e){
            CheckConnection();
            e.getMessage();
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Confirm delete country details ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                database.mainDao().reset(dataList);
                                dataList.clear();
                                recyclerView.removeAllViews();
                                countryAdapter.notifyDataSetChanged();

                                dataLayout.setVisibility(View.GONE);
                                noDataLayout.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = alertDialog.create();
                dialog.show();


            }
        });


    }

    public void CheckConnection() {
        offlineLayout.setVisibility(View.GONE);
        dataLayout.setVisibility(View.VISIBLE);
        noDataLayout.setVisibility(View.GONE);
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (null == networkInfo) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();

        } else {
            LoadData();
        }
    }

    private void LoadData() {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(COUNTRY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                System.out.println("DEBUG " + response);

                Data data = new Data();
                data.setText(response);

                database.mainDao().insert(data);

                Gson gson = new Gson();
                Country[] models = gson.fromJson(response, Country[].class);
                recyclerView.setAdapter(new CountryAdapter(getApplicationContext(), models));
                countryAdapter = new CountryAdapter(MainActivity.this, models);
                recyclerView.setAdapter(countryAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something wait wrong", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void Refresh(View view) {
        CheckConnection();
    }
}