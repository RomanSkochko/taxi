package com.romanskochko.taxi.features.ride.controller;

import com.romanskochko.taxi.core.model.dto.PageResponse;
import com.romanskochko.taxi.features.ride.dto.RideCreateDto;
import com.romanskochko.taxi.features.ride.dto.RideDto;
import com.romanskochko.taxi.features.ride.dto.RideEstimateDto;
import com.romanskochko.taxi.features.ride.dto.RideEstimateParams;
import com.romanskochko.taxi.features.ride.dto.RideListDto;
import com.romanskochko.taxi.features.ride.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RideController implements RideApi {

    RideService service;

    @GetMapping(ESTIMATE)
    public RideEstimateDto getRideEstimate(@Valid RideEstimateParams params) {
        return service.getEstimate(params);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(CREATED)
    @PostMapping(CREATE)
    public RideDto createRide(@Valid @RequestBody RideCreateDto dto, Principal principal) {
        return service.create(dto, principal);
    }

    @PreAuthorize("authentication != null && authentication.principal != null")
    @GetMapping(FIND_BY_ID)
    public RideDto getRideById(@PathVariable String id, Principal principal) {
        return service.getById(id, principal);
    }

    @PreAuthorize("authentication != null && authentication.principal != null")
    @GetMapping(GET_MY_RIDES_AS_PASSENGER)
    public PageResponse<RideListDto> getMyPassengerRides(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size,
                                                         Principal principal) {
        Page<RideListDto> ridesPage = service.getPassengerRides(page, size, principal);
        return PageResponse.fromPage(ridesPage);
    }

    @GetMapping(GET_MY_RIDES_AS_DRIVER)
    public Page<RideListDto> getMyDriverRides(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              Principal principal) {
        return service.getDriverRides(page, size, principal);
    }

    @PatchMapping(ACCEPT_RIDE)
    public RideDto acceptRide(@PathVariable String id, Principal principal) {
        return service.acceptRide(id, principal);
    }

    @PatchMapping(START_RIDE)
    public RideDto startRide(@PathVariable String id, Principal principal) {
        return service.startRide(id, principal);
    }

    @PatchMapping(COMPLETE_RIDE)
    public RideDto completeRide(@PathVariable String id, Principal principal) {
        return service.completeRide(id, principal);
    }

    @PatchMapping(CANCEL_RIDE)
    public RideDto cancelRide(@PathVariable String id,
                              @RequestParam(required = false) String reason,
                              Principal principal) {
        return service.cancelRide(id, principal);
    }
}
