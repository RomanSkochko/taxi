package com.romanskochko.taxi.features.passenger.listener;

import com.romanskochko.taxi.features.passenger.entity.PassengerProfile;
import com.romanskochko.taxi.features.passenger.repository.PassengerProfileRepository;
import com.romanskochko.taxi.features.user.entity.User;
import com.romanskochko.taxi.features.user.event.UserCreatedEvent;
import com.romanskochko.taxi.features.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PassengerProfileEventListener {

    private final PassengerProfileRepository passengerProfileRepository;
    private final UserService userService;

    //TODO if there will be runtime error, need implement retry mechanism
    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Handling UserCreatedEvent for userId: {}", event.userId());

        try {
            User user = userService.findById(event.userId());

            if (passengerProfileRepository.existsById(user.getId())) {
                log.warn("PassengerProfile already exists for userId: {}. Skipping creation.", event.userId());
                return;
            }

            PassengerProfile passengerProfile = PassengerProfile.builder()
                                                                .user(user)
                                                                .build();

            PassengerProfile profile = passengerProfileRepository.save(passengerProfile);
            log.info("PassengerProfile created successfully for userId: {}", event.userId());

        } catch (EntityNotFoundException e) {
            log.error("Cannot create PassengerProfile. User not found: {}", event.userId(), e);
        } catch (Exception e) {
            log.error("Failed to create PassengerProfile for userId: {}", event.userId(), e);
        }
    }
}