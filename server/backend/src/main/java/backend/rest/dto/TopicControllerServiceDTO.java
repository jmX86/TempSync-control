package backend.rest.dto;

import backend.domain.Topic;

public class TopicControllerServiceDTO {
    private String topicName;

    public TopicControllerServiceDTO(Topic topic){
        this.topicName = topic.getTopicName();
    }

    public String getTopicName() {
        return topicName;
    }
}
