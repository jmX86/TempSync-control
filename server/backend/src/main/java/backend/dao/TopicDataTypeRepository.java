package backend.dao;

import backend.domain.TopicDataType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicDataTypeRepository extends JpaRepository<TopicDataType, Long> {
}
