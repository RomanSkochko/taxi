package com.romanskochko.taxi.features.ride.repository;

import com.romanskochko.taxi.core.repository.BaseRepository;
import com.romanskochko.taxi.features.ride.entity.Ride;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends BaseRepository<Ride, String> {
}
