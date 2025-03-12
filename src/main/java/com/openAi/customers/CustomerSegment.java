package com.openAi.customers;

import lombok.Getter;

public enum CustomerSegment {
    START_UP(1),
    BIG_TECH(2),
    PROJECT(3);

    @Getter
    private final int value;

    CustomerSegment(int value) {
        this.value = value;
    }
}
