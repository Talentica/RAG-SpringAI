package com.openAi.security.enums;

import java.util.EnumMap;
import java.util.List;

/** TeamType consisting multiple team types. */
public enum TeamType {
  BIG_TECH,
  EARLY,
  GROWTH,
  PROFITABLE;
  private static final EnumMap<TeamType, List<TeamType>> CORRESPONDING_TYPES =
      new EnumMap<>(TeamType.class);

  static {
    CORRESPONDING_TYPES.put(BIG_TECH, List.of(BIG_TECH));
    CORRESPONDING_TYPES.put(EARLY, List.of(EARLY));
    CORRESPONDING_TYPES.put(GROWTH, List.of(GROWTH));
    CORRESPONDING_TYPES.put(PROFITABLE, List.of(PROFITABLE));
  }

    public static List<TeamType> getCorrespondingTypeList(TeamType teamType) {
    return CORRESPONDING_TYPES.get(teamType);
  }


}
