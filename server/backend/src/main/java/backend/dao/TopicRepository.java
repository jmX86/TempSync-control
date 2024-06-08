package backend.dao;

import backend.domain.Device;
import backend.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("SELECT top FROM TOPIC top WHERE top.topicName = ?2 AND top.topicDevice = ?1")
    Optional<Topic> findTopicByDeviceAndName(@Param("1") Device device, @Param("2") String topicName);
}
