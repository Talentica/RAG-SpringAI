package com.openAi.touchpoint.service;

import com.openAi.security.enums.EntityStatus;
import com.openAi.touchpoint.entities.TouchpointMeetingType;
import com.openAi.touchpoint.entities.TouchpointTopic;
import com.openAi.touchpoint.entities.TouchpointTopicStatus;
import com.openAi.touchpoint.model.MeetingModel;
import com.openAi.touchpoint.repository.TouchPointRepository;
import com.openAi.touchpoint.repository.TouchpointMeetingRepository;
import com.openAi.touchpoint.repository.TouchpointTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TouchpointService {
    private final TouchPointRepository touchpointRepository;
    private final TouchpointTopicRepository touchpointTopicRepository;
    private final ChatModel chatModel;
    private final TouchpointMeetingRepository touchpointMeetingRepository;

    public String updateTouchPointStatusForTeam(Integer customerId) {
        List<MeetingModel> models =  touchpointRepository.getTouchpointData(customerId);
        List<TouchpointTopic> roTopics = touchpointTopicRepository.findByStatusAndMeetingType("ACTIVE", TouchpointMeetingType.RO);
        List<TouchpointTopic> csoTopics = touchpointTopicRepository.findByStatusAndMeetingType("ACTIVE",TouchpointMeetingType.CSO);
        models.stream().limit(1).forEach(model->{
            String content = model.getMeetingContent();
            List<TouchpointTopic> topics = model.getMeetingType().equals("RO")?roTopics:csoTopics;
            List<TouchpointTopicStatus> topicStatuses = openAIToUpdateMeetingStatus(topics, content);
            touchpointMeetingRepository.findById(model.getMeetingId()).ifPresent(meeting->{
                meeting.setTouchpointTopicStatuses(topicStatuses);
                touchpointMeetingRepository.save(meeting);
            });
        });
        return "Success";
    }

    public List<TouchpointTopicStatus> openAIToUpdateMeetingStatus(List<TouchpointTopic> topics, String content) {

        String prompt = String.format("""
                    The meeting note is %s and the topics with its id are are %s.
                    Analyze the content and give the response with topic id and status as DISCUSSED,NOT_DISCUSSED or NOT_NEEDED;
                    """, content, topics.stream().map(topic-> topic.getId().toString()+"."+topic.getTopicName()).collect(Collectors.joining(",")));


        List<TouchpointTopicStatus> topicStatuses =
                ChatClient.builder(chatModel)
                .build().prompt()
                .user(prompt)
                .call()
                .entity(new ParameterizedTypeReference<List<TouchpointTopicStatus>>() {});

        return topicStatuses;


    }
}
