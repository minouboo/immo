package studi.immo.entity;



import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Accommodation extends GenericEntity {


    @Column
    private Double squareMeter;

    @Column
    private Integer rooms;

    @ManyToOne
    @JoinColumn (name="user_id")
    private User user;

    @ManyToOne
    private Address address;

}
