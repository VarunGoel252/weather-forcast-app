package com.apple.weatherforcastapp.services;

import com.apple.weatherforcastapp.accweather.AccWeatherService;
import com.apple.weatherforcastapp.accweather.pojos.ForcastModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Forcast Service that handles request from the contorller
 */
@Service
public class ForcastService {

    @Value("${cacheDuration}")
    private long cacheDuration;
    @Autowired
    private AccWeatherService accWeatherService;

    private Map<String, ForcastModel> cache = new HashMap<>();


    /**
     * Method to return the current and the forcasted weather for the given pincode
     * @param zipcode
     * @return ForcastModel
     */
    public ForcastModel getForcastForZipcode(String zipcode){
        if(cache.containsKey(zipcode)){
            ForcastModel model = cache.get(zipcode);
            long currTimeInMillis = Calendar.getInstance().getTimeInMillis();
            if((currTimeInMillis - model.getCachedTime()) <= cacheDuration*60*1000) {
                model.setFetchedFromCache(true);
                return model;
            }
        }
        ForcastModel model = accWeatherService.getForcastModel(zipcode);
        model.setCachedTime(Calendar.getInstance().getTimeInMillis());
        model.setFetchedFromCache(false);
        cache.put(zipcode,model);
        return model;
    }
}
