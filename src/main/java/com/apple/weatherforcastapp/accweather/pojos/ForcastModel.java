package com.apple.weatherforcastapp.accweather.pojos;

import lombok.Data;

/**
 * Model to return in response to the Weather API
 */
@Data
public class ForcastModel {
    String currentTemp;
    String weatherType;
    String maxTemp;
    String minTemp;
    String unitType;
    Boolean fetchedFromCache;
    Long cachedTime;
}
