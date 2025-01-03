package com.openAi.security.enums;

import lombok.Getter;

public enum EntityStatus {
  INACTIVE(0),
  ACTIVE(1);

  @Getter private final int value;

  EntityStatus(int value) {
    this.value = value;
  }
}
