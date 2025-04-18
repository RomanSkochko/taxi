package com.romanskochko.taxi.features.ride.controller;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/rides")
public interface RideApi {
    String BASE = "/api/v1/rides";
    String ESTIMATE = "/estimate";
    String CREATE = "";
    String FIND_BY_ID = "/{id}";
    String GET_MY_RIDES_AS_PASSENGER = "/passenger/me";
    String GET_MY_RIDES_AS_DRIVER = "/driver/me";
    String ACCEPT_RIDE = "/{id}/accept";
    String START_RIDE = "/{id}/start";
    String COMPLETE_RIDE = "/{id}/complete";
    String CANCEL_RIDE = "/{id}/cancel";
}
