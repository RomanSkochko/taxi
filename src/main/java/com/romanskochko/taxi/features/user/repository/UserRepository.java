package com.romanskochko.taxi.features.user.repository;

import com.romanskochko.taxi.core.repository.BaseRepository;
import com.romanskochko.taxi.features.user.entity.User;
import com.romanskochko.taxi.features.user.repository.projection.UserAuthView;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, String> {
    Optional<UserAuthView> findUserAuthByPhone(String phone);
}
