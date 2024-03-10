package com.apple.weatherforcastapp.accweather.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrentWeather {
    private String weatherType;
    private String temperature;
}
