package backend.service.impl;

import backend.dao.*;
import backend.domain.*;
import backend.rest.dto.CreateTopicDTO;
import backend.rest.dto.TopicControllerServiceDTO;
import backend.service.DeviceService;
import backend.service.TopicService;
import backend.service.exceptions.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class TopicServiceJpa implements TopicService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private TopicDataTypeRepository typeRepository;

    @Autowired
    private TopicDirectionRepository directionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Override
    public TopicControllerServiceDTO registerNewTopic(String userName, CreateTopicDTO topicDTO) {
        Assert.hasText(topicDTO.getTopicName(), "Topic name must be given");
        Assert.hasText(topicDTO.getTopicDevice(), "Topic device must be given.");
        // TODO: Assert all data requirements
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new DataNotFoundException("User not found!"));

        Device topicDevice = deviceRepository.findDeviceByNameAndOwner(topicDTO.getTopicDevice(), user).orElseThrow(() -> new DataNotFoundException("Device not found!"));
        TopicDirection direction = directionRepository.findById(topicDTO.getTopicDir()).orElseThrow(() -> new DataNotFoundException("Direction not found!"));
        TopicDataType dataType = typeRepository.findById(topicDTO.getTopicType()).orElseThrow(() -> new DataNotFoundException("Data type not found."));

        Topic newTopic = new Topic(topicDTO.getTopicName(), topicDevice, direction, dataType);

        return new TopicControllerServiceDTO(topicRepository.save(newTopic));
    }
}
