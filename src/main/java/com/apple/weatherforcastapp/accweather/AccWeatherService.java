package com.apple.weatherforcastapp.accweather;

import com.apple.weatherforcastapp.accweather.constants.Constants;
import com.apple.weatherforcastapp.accweather.pojos.CurrentWeather;
import com.apple.weatherforcastapp.accweather.pojos.Forcast;
import com.apple.weatherforcastapp.accweather.pojos.ForcastModel;
import com.apple.weatherforcastapp.accweather.pojos.RegionDetails;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Service that deals with calling of downstream accweather services to get the relevant data
 * More Details around the APIs : <a href="https://developer.accuweather.com/apis">...</a>
 */
@Service
public class AccWeatherService {
    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, String> zipcodeLocationKeyCache = new HashMap<>();

    @Value("${apikey}")
    private String apiKey;

    @Value("${locationFromZipcodeUrl}")
    private String locationFromZipcodeUrl;

    @Value("${dailyForcastUrl}")
    private String dailyForcastUrl;

    @Value("${currentConditionUrl}")
    private String currentConditionUrl;

    /**
     * Populates the basic parameters required for making the downstream call to external API
     *
     * @return Map<String, String>
     */
    private Map<String, String> getStandardParams() {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.API_KEY, apiKey);
        return params;
    }

    /**
     * Gets the location id assocaited with a Zipcode
     *
     * @param zipcode
     * @return String
     */
    private String getLocationKeyByZipcode(String zipcode) {
        // Memoizing the location key associated with zipcode as it stays fix
        if (zipcodeLocationKeyCache.containsKey(zipcode)) {
            return zipcodeLocationKeyCache.get(zipcode);
        }
        Map<String, String> params = getStandardParams();
        params.put(Constants.ZIPCODE_KEY, zipcode);

        RegionDetails[] result = null;

        try{
            result = restTemplate.getForObject(locationFromZipcodeUrl + Constants.API_QUERY_PARAM + "&q={q}",
                    RegionDetails[].class, params);
        } catch (Exception e){
            throw new RuntimeException("Downstream call failed " + e.getLocalizedMessage());
        }

        if (result == null) {
            throw new RuntimeException("Null response from downstream");
        }

        for (RegionDetails regionDetails : result) {
            if (regionDetails.getPrimaryPostalCode().equals(zipcode)) {
                zipcodeLocationKeyCache.put(zipcode, regionDetails.getKey());
                return regionDetails.getKey();
            }
        }

        return null;
    }

    /**
     * Gets one day forcast for the given Zipcode
     *
     * @param zipcode
     * @return Forcast
     */
    private Forcast getForcast(String zipcode) {
        String locationKey = getLocationKeyByZipcode(zipcode);
        JsonNode currentForcast = null;

        try {
            currentForcast = restTemplate.getForObject(dailyForcastUrl + "/" + locationKey + Constants.API_QUERY_PARAM,
                    JsonNode.class, getStandardParams());
        } catch (Exception e){
            throw new RuntimeException("Downstream call failed " + e.getLocalizedMessage());
        }

        if (currentForcast == null) {
            throw new RuntimeException("Null response from downstream");
        }

        Forcast forcast = new Forcast();
        if (currentForcast.has(Constants.DAILY_FORCAST)) {
            JsonNode dailyForcast = currentForcast.get(Constants.DAILY_FORCAST);
            JsonNode dailyForcastArr = dailyForcast.get(0);
            if (dailyForcastArr.has(Constants.TEMPERATURE)) {
                JsonNode temperature = dailyForcastArr.get(Constants.TEMPERATURE);
                if (temperature.has(Constants.MINIMUM)) {
                    JsonNode minimum = temperature.get(Constants.MINIMUM);
                    if (minimum.has(Constants.VALUE)) {
                        forcast.setMinTemp(minimum.get(Constants.VALUE).asText());
                    }
                }
                if (temperature.has(Constants.MAXIMUM)) {
                    JsonNode maximum = temperature.get(Constants.MAXIMUM);
                    if (maximum.has(Constants.VALUE)) {
                        forcast.setMaxTemp(maximum.get(Constants.VALUE).asText());
                    }
                }
            }
        }

        return forcast;
    }

    /**
     * Gets the current weather for a given Zipcode
     *
     * @param zipcode
     * @return CurrentWeather
     */
    private CurrentWeather getCurrentCondition(String zipcode) {
        String locationKey = getLocationKeyByZipcode(zipcode);
        JsonNode result = null;

        try {
            result = restTemplate.getForObject(currentConditionUrl + "/" + locationKey + Constants.API_QUERY_PARAM,
                    JsonNode.class, getStandardParams());
        } catch (Exception e){
            throw new RuntimeException("Downstream call failed " + e.getLocalizedMessage());
        }

        if (result == null) {
            throw new RuntimeException("Null response from downstream");
        }

        JsonNode currentCondition = result.get(0);
        CurrentWeather currentWeather = new CurrentWeather();
        if (currentCondition.has(Constants.WEATHER_TEXT)) {
            currentWeather.setWeatherType(currentCondition.get(Constants.WEATHER_TEXT).asText());
        }
        if (currentCondition.has(Constants.TEMPERATURE)) {
            JsonNode temperature = currentCondition.get(Constants.TEMPERATURE);
            if (temperature.has(Constants.IMPERIAL)) {
                JsonNode imperial = temperature.get(Constants.IMPERIAL);
                if (imperial.has(Constants.VALUE)) {
                    currentWeather.setTemperature(imperial.get(Constants.VALUE).asText());
                }
            }
        }
        return currentWeather;
    }

    /**
     * Method that collated the current weather and forcasted weather for the given Zipcode
     *
     * @param zipcode
     * @return ForcastModel
     */
    public ForcastModel getForcastModel(String zipcode) {
        Forcast forcast = getForcast(zipcode);
        CurrentWeather currentConditions = getCurrentCondition(zipcode);

        ForcastModel forcastModel = new ForcastModel();
        forcastModel.setCurrentTemp(currentConditions.getTemperature());
        forcastModel.setWeatherType(currentConditions.getWeatherType());
        forcastModel.setMaxTemp(forcast.getMaxTemp());
        forcastModel.setMinTemp(forcast.getMinTemp());
        forcastModel.setUnitType("F");

        return forcastModel;
    }
}
