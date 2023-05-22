package com.example.helloapi;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("temperature_2m")
    private Temperature temperature;

    @SerializedName("wind_speed_10m")
    private WindSpeed windSpeed;

    public class Temperature {
        @SerializedName("unit")
        private String unit;

        @SerializedName("values")
        private float[] values;
    }

    public class WindSpeed {
        @SerializedName("unit")
        private String unit;

        @SerializedName("values")
        private float[] values;
    }
}

