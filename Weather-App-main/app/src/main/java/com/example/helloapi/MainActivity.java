package com.example.helloapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    private RequestQueue mRequestQueue;
    private WeatherApiService mApiService;
    private TextView tvName;
    private TextView tvTime;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRequestQueue = Volley.newRequestQueue(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiService = retrofit.create(WeatherApiService.class);

        Button buttonVolley = findViewById(R.id.btnRequest);
        Button buttonRetrofit = findViewById(R.id.btnRetrofit);
        buttonVolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithVolley();
            }
        });

        buttonRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithRetrofit();
            }
        });

    }

    private void sendRequestWithVolley() {
        double latitude = -7.93;
        double longitude = 112.61;
        String hourly = "temperature_2m,wind_speed_80m";

        String url = "https://api.open-meteo.com/v1/forecast?latitude=-7.93&longitude=112.61&hourly=temperature_2m&daily=temperature_2m_max,temperature_2m_min" + latitude +
                "&longitude=" + longitude + "&hourly=" + hourly;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String latitude = response.getJSONObject(0).getString("latitude");
                            String longitude = response.getJSONObject(0).getString("longitude");
                            String temperature = response.getJSONObject(0).getJSONObject("hourly").getJSONArray("temperature_2m").toString();

                            Intent intent = new Intent(MainActivity.this, Hasil.class);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            intent.putExtra("temperature", temperature);

                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        Toast.makeText(MainActivity.this, "Volley Error" + error, Toast.LENGTH_SHORT).show();
                    }
                });
        mRequestQueue.add(request);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    private void sendRequestWithRetrofit() {
        double latitude = -7.93;
        double longitude = 112.61;
        String hourly = "temperature_2m,wind_speed_80m";

        Call<Data> call = mApiService.getWeatherData(latitude, longitude, hourly);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    Data weatherData = response.body();
                    Toast.makeText(MainActivity.this, "Retrofit Response Received", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Retrofit Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onResponse(Call<Data> call, retrofit2.Response<Data> response) {

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Retrofit Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}