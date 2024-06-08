package backend.service;

import backend.domain.User;
import backend.rest.dto.ReadingControllerServiceDTO;

public interface ReadingService {
    boolean saveNewReading(String userName, String deviceName, String subtopicName, String payload);

    ReadingControllerServiceDTO getLatestReadingForTopic(String userName, String topicName, String deviceName);
}
