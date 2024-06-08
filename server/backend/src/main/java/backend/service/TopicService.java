package backend.service;

import backend.rest.dto.CreateTopicDTO;
import backend.rest.dto.TopicControllerServiceDTO;

public interface TopicService {

    TopicControllerServiceDTO registerNewTopic(String userName, CreateTopicDTO topicDTO);
}
