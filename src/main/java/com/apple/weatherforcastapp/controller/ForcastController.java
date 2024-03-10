package com.apple.weatherforcastapp.controller;

import com.apple.weatherforcastapp.services.ForcastService;
import com.apple.weatherforcastapp.accweather.pojos.ForcastModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that exposes APIs relates to weather forcasting
 */
@RestController
@RequestMapping("/api")
public class ForcastController {

    @Autowired
    ForcastService forcastService;

    /**
     * API to give the current weather condition and forcast for 1 Day
     * @param zipcode
     * @return ForcastModal
     */
    @GetMapping("/weather/{zipcode}")
    public ForcastModel getForcastForZipcode(@PathVariable String zipcode){
        return forcastService.getForcastForZipcode(zipcode);
    }
}
