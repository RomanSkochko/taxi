package com.romanskochko.taxi.features.ride.service;

import com.neovisionaries.i18n.CurrencyCode;
import com.romanskochko.taxi.core.exception.CustomApplicationException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.OffsetDateTime;

import static com.romanskochko.taxi.core.exception.ErrorCode.ACCESS_DENIED;
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
        User currentUser = getCurrentUser(principal);

        PassengerProfile profile = passengerRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Passenger profile not found, id: " + currentUser.getId()));

        Ride ride = mapper.toRide(dto);
        ride.setPassengerProfile(profile);
        ride.setCreateTime(OffsetDateTime.now());
        ride.setStatus(RideStatus.CREATED);

        return mapper.toDto(repository.save(ride));
    }

    @Transactional(readOnly = true)
    public RideDto getById(String id, Principal principal) {
        User currentUser = getCurrentUser(principal);

        Ride ride = findById(id);

        if (!ride.getPassengerProfile().getId().equals(currentUser.getId()))
            throw new CustomApplicationException(ACCESS_DENIED.getValue(), ACCESS_DENIED);

        return mapper.toDto(ride);
    }

    @Transactional(readOnly = true)
    public Page<RideListDto> getPassengerRides(int page, int size, Principal principal) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());

        User currentUser = getCurrentUser(principal);

        return repository.findAllByPassengerProfileId(currentUser.getId(), pageable);
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
