package com.openAi.touchpoint.entities;

import lombok.Getter;

public enum TouchpointTopicStatuses {
  DISCUSSED(0),
  NOT_DISCUSSED(1),
  NOT_NEEDED(2);

  @Getter
  private final int value;

  TouchpointTopicStatuses(int value) {
    this.value = value;
  }
}
