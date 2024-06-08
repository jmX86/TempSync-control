package backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity(name = "ROLES")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name", unique = true, nullable = false)
    @Size(max = 128)
    private String roleName;

    @OneToMany(mappedBy = "userRole")
    private Set<User> roleUsers;

    public Role() {
        this.roleName = null;
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public @Size(max = 128) String getRoleName() {
        return roleName;
    }

    public void setRoleName(@Size(max = 128) String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getRoleUsers() {
        return roleUsers;
    }
}
