package backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;

@Entity(name = "READINGS")
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reading_id")
    private Long readingId;

    @Column(name = "reading_value", nullable = false)
    @Size(max = 128)
    private String readingValue;

    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "topic_id")
    private Topic readingTopic;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reading_timestamp", nullable = false)
    private Timestamp readingTimestamp;

    public Reading(String readingValue, Topic readingTopic, Timestamp readingTimestamp) {
        this.readingValue = readingValue;
        this.readingTopic = readingTopic;
        this.readingTimestamp = readingTimestamp;
    }

    public Reading() {
        this.readingValue = null;
        this.readingTopic = null;
        this.readingTimestamp = null;
    }

    public @Size(max = 128) String getReadingValue() {
        return readingValue;
    }

    public void setReadingValue(@Size(max = 128) String readingValue) {
        this.readingValue = readingValue;
    }

    public Long getReadingId() {
        return readingId;
    }

    public Topic getReadingTopic() {
        return readingTopic;
    }

    public Timestamp getReadingTimestamp() {
        return readingTimestamp;
    }
}
