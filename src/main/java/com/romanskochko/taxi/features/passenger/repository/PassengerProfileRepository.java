package com.romanskochko.taxi.features.passenger.repository;

import com.romanskochko.taxi.core.repository.BaseRepository;
import com.romanskochko.taxi.features.passenger.entity.PassengerProfile;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerProfileRepository extends BaseRepository<PassengerProfile, String> {
}
