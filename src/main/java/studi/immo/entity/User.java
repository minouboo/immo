package studi.immo.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Setter
@Getter
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends GenericEntity{

    @Column(unique = true)
    private String userName;

    @Column
    private String lastName;

    @Column
    private String firstName;

    @Column (unique = true)
    private String email;

    @NotNull
    @Column
    private String password;

    @ManyToOne
    private Address address;

    @OneToOne (cascade = CascadeType.REMOVE)
    @JoinColumn (name = "cash_id")
    private Cash cash;

    @ElementCollection (targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable (name="user_role")
    @Column (name="role")
    @Enumerated (value = EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Column (name = "enabled")
    private Boolean isEnabled = true;



}
