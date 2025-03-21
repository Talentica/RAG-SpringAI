package com.openAi.touchpoint.entities;

import com.openAi.customers.Customer;
import com.openAi.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "touchpoint_meeting")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class TouchpointMeeting {

  @Id
  @Column(name = "id", columnDefinition = "INT(11) UNSIGNED")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "touchpoint_date")
  private Date touchpointDate;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "content_id")
  private TouchpointMeetingContent touchpointMeetingContent;

  @Column(columnDefinition = "TEXT")
  private String meetingSummary;

  private String subject;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "touchpoint_meeting_participants",
      joinColumns = @JoinColumn(name = "touchpoint_meeting_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "participants_id", referencedColumnName = "id"))
  private Set<TouchpointParticipant> touchpointParticipants;

  @Enumerated(EnumType.STRING)
  @Column(name = "meeting_type")
  private TouchpointMeetingType meetingType;

  @Enumerated(EnumType.STRING)
  @Column(name = "customer_visit_type")
  private CustomerVisitType customerVisitType = CustomerVisitType.NOT_HAPPENED;

  @Column(name = "pain_points")
  private Integer painPoints;

  @Column(name = "gain_points")
  private Integer gainPoints;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "touchpoint_meeting_statuses",
      joinColumns = @JoinColumn(name = "touchpoint_meeting_id", referencedColumnName = "id"),
      inverseJoinColumns =
      @JoinColumn(name = "touchpoint_topic_status_id", referencedColumnName = "id"))
  private List<TouchpointTopicStatus> touchpointTopicStatuses;
}
