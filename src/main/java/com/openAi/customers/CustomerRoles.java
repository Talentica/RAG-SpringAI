package com.openAi.customers;

import lombok.Getter;

public enum CustomerRoles {
    CSO(1),
    RO(2),
    TO(3);

    @Getter
    private final int value;

    CustomerRoles(int value) {
        this.value = value;
    }
}
