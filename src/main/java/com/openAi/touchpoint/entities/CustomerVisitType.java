package com.openAi.touchpoint.entities;

import lombok.Getter;

public enum CustomerVisitType {
    NOT_HAPPENED(1),
    CUSTOMER_LOCATION(2),
    TALENTICA(3);

    @Getter
    private final int value;

    CustomerVisitType(int value) {
        this.value = value;
    }
}
