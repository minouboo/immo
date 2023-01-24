package studi.immo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
public class Accommodation extends GenericEntity {

    @Column
    private Double squareMeter;

    @Column
    private Integer rooms;

    @ManyToOne
    private User user;

    @ManyToOne
    private Address address;

}
