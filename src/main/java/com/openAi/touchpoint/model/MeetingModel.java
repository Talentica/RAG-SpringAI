package com.openAi.touchpoint.model;

import lombok.Data;

@Data
public class MeetingModel {
    private Integer meetingId;
    private String meetingType;
    private String touchPointDate;
    private String meetingContent;
}
