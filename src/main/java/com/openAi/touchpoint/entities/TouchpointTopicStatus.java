package com.openAi.touchpoint.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@Entity
@Table(name = "touchpoint_topic_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TouchpointTopicStatus {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(name = "topic_status")
  private TouchpointTopicStatuses topicStatus;

  @ManyToOne
  @JoinColumn(name = "touchpoint_topic_id")
  private TouchpointTopic touchpointTopic;
}
