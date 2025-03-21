package com.openAi.touchpoint.repository;

import com.openAi.security.enums.EntityStatus;
import com.openAi.touchpoint.entities.TouchpointMeetingType;
import com.openAi.touchpoint.entities.TouchpointTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouchpointTopicRepository extends JpaRepository<TouchpointTopic, Integer> {
    List<TouchpointTopic> findByStatusAndMeetingType(String status, TouchpointMeetingType meetingType);

}