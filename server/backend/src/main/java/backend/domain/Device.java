package backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity(name = "DEVICE")
@Table(uniqueConstraints =
        { @UniqueConstraint(
                name = "device_unique_constraint",
                columnNames = { "device_name", "device_owner" }
        )}
)
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long deviceId;

    @Size(max = 64)
    @Column(name = "device_name", nullable = false)
    private String deviceName;  // used in topics: <user_name>/<"up" or "down">/<device_name>/<topic_name>

    @ManyToOne
    @JoinColumn(name = "device_owner", referencedColumnName = "user_id", nullable = false)
    private User deviceOwner;

    @OneToMany(mappedBy = "topicDevice")
    private Set<Topic> deviceTopics;

    public Device(){
        this.deviceName = null;
        this.deviceOwner = null;
    }

    public Device(String deviceName, User deviceOwner) {
        this.deviceName = deviceName;
        this.deviceOwner = deviceOwner;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public @Size(max = 64) String getDeviceName() {
        return deviceName;
    }

    public User getDeviceOwner() {
        return deviceOwner;
    }

    public Set<Topic> getDeviceTopics() {
        return deviceTopics;
    }

    public void setDeviceName(@Size(max = 64) String deviceName) {
        this.deviceName = deviceName;
    }
    
    public void setDeviceOwner(User deviceOwner) {
        this.deviceOwner = deviceOwner;
    }
}
