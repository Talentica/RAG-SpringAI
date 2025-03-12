package com.openAi.user;

import lombok.Getter;

public enum UserRole {
    ADMIN(0),
    MANAGER(1),
    LEAD(2),
    M4(3),
    CSO(4),
    RO(5),
    TO(6),
    I3(7),
    I2(8),
    I1(9),
    WATCHER(10),
    PEOPLE_GROUP(11),
    M1(12),
    M2(13),
    I4(14),
    A1(15),
    A2(16),
    A3(17),
    M3(18),
    CUSTOMER(19),
    E1(20),
    E2(21);

    @Getter
    private final int value;

    UserRole(int value) {
        this.value = value;
    }
}

