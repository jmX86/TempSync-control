package backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// A new connection each user can add that allows more connections to the same topic namespace as user
@Entity(name = "CONNECTIONS")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conn_id")
    private Long connectionId;

    @NotNull
    @Column(name = "conn_mqtt_id", unique = true)
    @Size(max = 128)
    private String connectionMqttId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User connectionUser;

    @NotNull
    @Size(max = 64)
    @Column(name = "conn_name")
    private String connectionName;

    /*
    * TODO: Napravit da user može dodati connection s nazivom,
        to pokrene se automatski dodavanje usera sa usernameom: userName-connectionName i tom useru se pridjeli
        role username-role
    * */
}
