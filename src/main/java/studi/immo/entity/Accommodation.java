package studi.immo.entity;



import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
