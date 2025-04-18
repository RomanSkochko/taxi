package com.romanskochko.taxi.features.ride.service;

import com.neovisionaries.i18n.CurrencyCode;
import com.romanskochko.taxi.core.service.BaseService;
import com.romanskochko.taxi.features.passenger.entity.PassengerProfile;
import com.romanskochko.taxi.features.passenger.repository.PassengerProfileRepository;
import com.romanskochko.taxi.features.ride.dto.RideCreateDto;
import com.romanskochko.taxi.features.ride.dto.RideDto;
import com.romanskochko.taxi.features.ride.dto.RideEstimateDto;
import com.romanskochko.taxi.features.ride.dto.RideEstimateParams;
import com.romanskochko.taxi.features.ride.dto.RideListDto;
import com.romanskochko.taxi.features.ride.entity.Ride;
import com.romanskochko.taxi.features.ride.enums.RideStatus;
import com.romanskochko.taxi.features.ride.mapper.RideMapper;
import com.romanskochko.taxi.features.ride.repository.RideRepository;
import com.romanskochko.taxi.features.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.OffsetDateTime;

import static lombok.AccessLevel.PRIVATE;

@Service
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RideService extends BaseService<Ride, String> {

    RideRepository repository;
    PassengerProfileRepository passengerRepository;
    RideMapper mapper;

    public RideEstimateDto getEstimate(@Valid RideEstimateParams params) {
        //TODO logic
        return new RideEstimateDto(250, CurrencyCode.UAH.getName());
    }

    @Transactional
    public RideDto create(RideCreateDto dto, Principal principal) {

        User user = getCurrentUser(principal);

        PassengerProfile profile = passengerRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Passenger profile not found, id: " + user.getId()));

        Ride ride = mapper.toRide(dto);
        ride.setPassengerProfile(profile);
        ride.setCreateTime(OffsetDateTime.now());
        ride.setStatus(RideStatus.LOOKING_FOR_DRIVER);

        return mapper.toDto(repository.save(ride));
    }

    public RideDto getById(String id, Principal principal) {
        return null;
    }

    public Page<RideListDto> getPassengerRides(int page, int size, Principal principal) {
        return null;
    }

    public Page<RideListDto> getDriverRides(int page, int size, Principal principal) {
        return null;
    }

    public RideDto acceptRide(String id, Principal principal) {
        return null;
    }

    public RideDto startRide(String id, Principal principal) {
        return null;
    }

    public RideDto completeRide(String id, Principal principal) {
        return null;
    }

    public RideDto cancelRide(String id, Principal principal) {
        return null;
    }

    private User getCurrentUser(Principal principal) {
        Object principalObj = ((Authentication) principal).getPrincipal();
        if (principalObj instanceof User user) {
            return user;
        }
        throw new AccessDeniedException("Authenticated principal is not a valid User");
    }

    @Override
    protected Class<Ride> entityClass() {
        return Ride.class;
    }
}
