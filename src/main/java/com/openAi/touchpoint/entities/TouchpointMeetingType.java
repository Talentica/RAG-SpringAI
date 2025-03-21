package com.openAi.touchpoint.entities;

import lombok.Getter;

public enum TouchpointMeetingType {
  CSO(1),
  RO(2),
  TO(3);

  @Getter
  private final int value;

  TouchpointMeetingType(int value) {
    this.value = value;
  }
}
