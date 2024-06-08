package backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity(name = "TOPIC_DIR")
public class TopicDirection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_dir_id")
    private Long topicDirId;

    @Column(name = "topic_dir_desc")
    @Size(max = 512)
    private String topicDirDesc;

    @OneToMany(mappedBy = "topicDirection")
    private Set<Topic> dirTopics;
}
