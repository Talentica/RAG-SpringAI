package com.openAi.touchpoint.repository;

import com.openAi.touchpoint.entities.TouchpointMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouchpointMeetingRepository extends JpaRepository<TouchpointMeeting,Integer> {
}
