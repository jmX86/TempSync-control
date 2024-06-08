package backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity(name = "TOPIC")
@Table(uniqueConstraints =
        { @UniqueConstraint(
                name = "topic_unique_constraint",
                columnNames = { "device_id", "topic_name" }
        )}
)
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long topicId;

    @NotNull
    @Column(name = "topic_name")
    @Size(max = 64)
    private String topicName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "device_id")
    private Device topicDevice;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "topic_dir_id", referencedColumnName = "topic_dir_id")
    private TopicDirection topicDirection;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "topic_data_id", referencedColumnName = "topic_data_id")
    private TopicDataType topicDataType;

    @OneToMany(mappedBy = "readingTopic")
    private Set<Reading> topicReadings;

    public Topic(String topicName, Device topicDevice, TopicDirection topicDirection, TopicDataType topicDataType) {
        this.topicName = topicName;
        this.topicDevice = topicDevice;
        this.topicDirection = topicDirection;
        this.topicDataType = topicDataType;
    }

    public Topic() {
        this.topicName = null;
        this.topicDevice = null;
        this.topicDirection = null;
        this.topicDataType = null;
    }

    public @NotNull @Size(max = 64) String getTopicName() {
        return topicName;
    }

    public void setTopicName(@NotNull @Size(max = 64) String topicName) {
        this.topicName = topicName;
    }

    public @NotNull Device getTopicDevice() {
        return topicDevice;
    }

    public void setTopicDevice(@NotNull Device topicDevice) {
        this.topicDevice = topicDevice;
    }

    public @NotNull TopicDirection getTopicDirection() {
        return topicDirection;
    }

    public void setTopicDirection(@NotNull TopicDirection topicDirection) {
        this.topicDirection = topicDirection;
    }

    public @NotNull TopicDataType getTopicDataType() {
        return topicDataType;
    }

    public void setTopicDataType(@NotNull TopicDataType topicDataType) {
        this.topicDataType = topicDataType;
    }

    public Set<Reading> getTopicReadings() {
        return topicReadings;
    }

    public Long getTopicId() {
        return topicId;
    }
}
