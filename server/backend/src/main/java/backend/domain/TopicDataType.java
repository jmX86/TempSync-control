package backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity(name = "TOPIC_DATA")
public class TopicDataType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_data_id")
    private Long topicDataTypeId;

    @Column(name = "topic_data_desc")
    @Size(max = 512)
    private String topicDataTypeDesc;

    @OneToMany(mappedBy = "topicDataType")
    private Set<Topic> dataTopics;
}
