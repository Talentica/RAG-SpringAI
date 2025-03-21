package com.openAi.touchpoint.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "touchpoint_meeting_content_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouchpointMeetingContentCategory {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "category_name")
	private String categoryName;
	
}
