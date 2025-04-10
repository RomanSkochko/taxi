package com.romanskochko.taxi.features.user.repository.projection;


import com.romanskochko.taxi.core.model.enums.Role;

import java.util.Set;

public interface UserAuthView {
    String getId();

    String getName();

    String getPassword();

    String getPhone();

    Set<Role> getRoles();
}
