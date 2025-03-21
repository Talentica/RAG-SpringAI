package com.openAi.touchpoint.entities;

import com.openAi.security.enums.EntityStatus;
import com.openAi.touchpoint.entities.TouchpointFrequencyExpected;
import com.openAi.touchpoint.entities.TouchpointMeetingType;
import com.openAi.touchpoint.entities.TouchpointPillar;
import com.openAi.touchpoint.entities.TouchpointTopicType;
import lombok.*;
import lombok.experimental.Accessors;

import jakarta.persistence.*;


@Entity
@Table(name = "touchpoint_topic")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouchpointTopic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "topic_name")
    private String topicName;

    @ManyToOne
    @JoinColumn(name = "touchpoint_pillar_id")
    private TouchpointPillar touchpointPillar;

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_type")
    private TouchpointMeetingType meetingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency_expected")
    private TouchpointFrequencyExpected frequencyExpected;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic_type")
    private TouchpointTopicType topicType;

    @Enumerated(EnumType.STRING)
    private EntityStatus status = EntityStatus.ACTIVE;

}
