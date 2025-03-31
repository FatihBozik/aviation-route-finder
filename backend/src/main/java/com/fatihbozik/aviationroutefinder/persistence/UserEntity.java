package com.fatihbozik.aviationroutefinder.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private Set<RoleEntity> roles;

    @JsonIgnore
    public void addRole(String roleName) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleName);
        this.roles.add(roleEntity);
    }
}
