package backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.integration.annotation.Default;

import java.util.Set;
import java.util.TimeZone;

@Entity(name = "USER_TABLE")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "user_email", unique = true)
    @Size(max=128)
    private String userEmail;

    @NotNull
    @Column(name = "user_name", unique = true)
    @Size(min = 6, max = 64)
    private String userName;

    @NotNull
    @Column(name = "pass_hash")
    private String passwordHash;

    @NotNull
    @Column(name = "user_broker_link")
    private boolean addedToBroker;

    @Column(name = "user_timezone")
    private String userTimezone;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role userRole;

    @OneToMany(mappedBy = "deviceOwner")
    private Set<Device> devices;

    @OneToMany(mappedBy = "connectionUser")
    private Set<Connection> connections;

    public User(){
        this.userEmail = null;
        this.userName = null;
        this.passwordHash = null;
        this.userRole = null;
        this.userTimezone = null;
        this.addedToBroker = false;
    }

    public User(String userEmail, String userName, String passwordHash, Role userRole) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.userRole = userRole;
        this.addedToBroker = false;
        this.userTimezone = null;
    }

    public Long getUserId() {
        return userId;
    }

    public @NotNull @Size(max = 128) String getUserEmail() {
        return userEmail;
    }

    public @NotNull @Size(min = 6, max = 64) String getUserName() {
        return userName;
    }

    public @NotNull String getPasswordHash() {
        return passwordHash;
    }

    public @NotNull Role getUserRole() {
        return userRole;
    }

    public Set<Device> getDevices() {
        return devices;
    }
    
    public void setUserEmail(@NotNull @Size(max = 128) String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(@NotNull @Size(min = 6, max = 64) String userName) {
        this.userName = userName;
    }

    public void setPasswordHash(@NotNull String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setUserRole(@NotNull Role userRole) {
        this.userRole = userRole;
    }

    @NotNull
    public boolean isAddedToBroker() {
        return addedToBroker;
    }

    public void setAddedToBroker(@NotNull boolean addedToBroker) {
        this.addedToBroker = addedToBroker;
    }

    public Set<Connection> getConnections() {
        return connections;
    }

    public String getUserTimezone() {
        return userTimezone;
    }

    public void setUserTimezone(TimeZone userTimezone) {
        this.userTimezone = userTimezone.getDisplayName();
    }
}
