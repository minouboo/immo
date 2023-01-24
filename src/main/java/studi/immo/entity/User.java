package studi.immo.entity;


import jakarta.persistence.*;
import lombok.*;


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

    @Column
    private String password;

    @ManyToOne
    private Address address;


}
