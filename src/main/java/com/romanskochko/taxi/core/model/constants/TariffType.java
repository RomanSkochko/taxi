package com.romanskochko.taxi.core.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum TariffType {
    ECONOM(1.0),
    STANDARD(1.2),
    COMFORT(1.5),
    UNIVERSAL(1.5),
    ECO(1.2);

    double multiplier;
}
