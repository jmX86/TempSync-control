package backend.dao;

import backend.domain.Reading;
import backend.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReadingRepository extends JpaRepository<Reading, Long> {
    @Transactional
    @Modifying
    @Query(
            value = "INSERT INTO readings(reading_value, topic_id, reading_timestamp)" +
                    "VALUES (?1, ?2, CURRENT_TIMESTAMP)",
            nativeQuery = true
    )
    void insertReadingWithTS(@Param("1") String readingValue, @Param("2") Long topicId);

    @Query("SELECT reading FROM READINGS reading WHERE " +
            "reading.readingTopic.topicDevice.deviceOwner.userName = ?1 " +
            "AND reading.readingTopic.topicDevice.deviceName = ?3 " +
            "AND reading.readingTopic.topicName = ?2 " +
            "ORDER BY reading.readingTimestamp DESC " +
            "LIMIT 1")
    Optional<Reading> findLatestReadingOfTopic(@Param("1") String userName, @Param("2") String topicName, @Param("3") String deviceName);
}
