package com.openAi.touchpoint.entities;

import lombok.Getter;

public enum TouchpointParticipantRole {
	CSO(1),
	RO(2),
	TO(3);
	
	@Getter
    private final int value;

	TouchpointParticipantRole(int value) {
        this.value = value;
    }
}
