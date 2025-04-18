package com.romanskochko.taxi.core.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Embeddable
public class Location implements Serializable {

    @Column(columnDefinition = "geometry(Point,4326)")
    Point point;

    public double getLatitude() {
        return point.getY();
    }

    public double getLongitude() {
        return point.getX();
    }

    public static Location of(double latitude, double longitude) {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        return new Location(gf.createPoint(new Coordinate(longitude, latitude)));
    }

}
