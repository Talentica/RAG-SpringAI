package com.openAi.touchpoint.entities;

import lombok.Getter;

public enum TouchpointFrequencyExpected {
  SPRINT(1),
  QUARTERLY(2),
  MONTHLY(3);

  @Getter
  private final int value;

  TouchpointFrequencyExpected(int value) {
    this.value = value;
  }
}
