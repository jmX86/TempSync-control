package backend.service.impl;

import backend.dao.DeviceRepository;
import backend.dao.ReadingRepository;
import backend.dao.TopicRepository;
import backend.dao.UserRepository;
import backend.domain.Device;
import backend.domain.Reading;
import backend.domain.Topic;
import backend.domain.User;
import backend.service.exceptions.DataNotFoundException;
import backend.rest.dto.ReadingControllerServiceDTO;
import backend.service.ReadingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ReadingServiceJpa implements ReadingService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ReadingRepository readingRepository;

    private final Logger logger = LoggerFactory.getLogger(ReadingServiceJpa.class);

    @Override
    public boolean saveNewReading(String userName, String deviceName, String subtopicName, String payload) {
        try {
            Assert.hasText(userName, "No username.");
            Assert.hasText(deviceName, "No device.");
            Assert.hasText(subtopicName, "No subtopic.");
            Assert.hasText(payload, "No payload.");

            Assert.isTrue(payload.length() <= 128, "Payload too big. Max. 128 char.");

            // TODO: Replace getting user, device and topic with one query with inputs userName, deviceName and subtopicName
            User user = userRepository.findByUserName(userName).orElseThrow(() -> new DataNotFoundException("User not found."));
            Device device = deviceRepository.findDeviceByNameAndOwner(deviceName, user).orElseThrow(() -> new DataNotFoundException("Device not found"));
            Topic topic = topicRepository.findTopicByDeviceAndName(device, subtopicName).orElseThrow(() -> new DataNotFoundException("Topic not found."));

            readingRepository.insertReadingWithTS(payload, topic.getTopicId());
        }catch (Exception e){
            logger.error(e.toString());
            return false;
        }

        return true;
    }

    @Override
    public ReadingControllerServiceDTO getLatestReadingForTopic(String userName, String topicName, String deviceName) {
        Reading reading = readingRepository.findLatestReadingOfTopic(userName, topicName, deviceName).orElseThrow(() -> new DataNotFoundException("There is no latest reading."));

        return new ReadingControllerServiceDTO(reading);
    }
}
