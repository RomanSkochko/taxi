package com.romanskochko.taxi.core.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Embeddable
public class Rating implements Serializable {
    double rating;
    int numberOfRatedRides;

    public void updateRating(int rideRate) {
        if (rideRate < 1 || rideRate > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        rating = ((rating * numberOfRatedRides) + rideRate) / (++numberOfRatedRides);
    }

    public static Rating createInitialRating() {
        Rating rating = new Rating();
        rating.rating = 0.0;
        rating.numberOfRatedRides = 0;
        return rating;
    }
}
