package com.openAi.touchpoint.entities;

import lombok.Getter;

public enum TouchpointTopicType {
  UNDERSTAND(1),
  SHARE(2);

  @Getter
  private final int value;

  TouchpointTopicType(int value) {
    this.value = value;
  }
}
