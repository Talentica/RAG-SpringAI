package com.openAi.touchpoint.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "touchpoint_meeting_content")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouchpointMeetingContent {

	@Id
	@Column(name = "id", columnDefinition = "INT(11) UNSIGNED")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private TouchpointMeetingContentCategory category;
	
}
