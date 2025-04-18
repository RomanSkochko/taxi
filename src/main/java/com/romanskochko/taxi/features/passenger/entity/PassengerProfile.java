package com.romanskochko.taxi.features.passenger.entity;

import com.romanskochko.taxi.core.entity.BaseEntity;
import com.romanskochko.taxi.features.ride.entity.Ride;
import com.romanskochko.taxi.features.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "passenger_profiles")
@DynamicUpdate
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PassengerProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "passengerProfile")
    List<Ride> rides;
}
