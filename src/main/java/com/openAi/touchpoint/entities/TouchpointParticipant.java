package com.openAi.touchpoint.entities;

import com.openAi.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "touchpoint_participants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TouchpointParticipant {

  @Id
  @Column(name = "id", columnDefinition = "INT(11) UNSIGNED")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "participant_name")
  private String participantName;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private TouchpointParticipantRole role;
}
