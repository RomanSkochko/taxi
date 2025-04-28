package com.romanskochko.taxi.features.ride.repository;

import com.romanskochko.taxi.core.repository.BaseRepository;
import com.romanskochko.taxi.features.ride.dto.RideListDto;
import com.romanskochko.taxi.features.ride.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends BaseRepository<Ride, String> {

    @Query("""
        SELECT new com.romanskochko.taxi.features.ride.dto.RideListDto(
            r.id,
            r.scheduledTime,
            r.endTime
        )
        FROM Ride r
        WHERE r.passengerProfile.id = :passengerId
        """)
    Page<RideListDto> findAllByPassengerProfileId(String passengerId, Pageable pageable);
}
