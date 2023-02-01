package studi.immo.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Address extends GenericEntity{


    @Column
    private Long streetNumber;

    @Column
    private String streetName;

    @ManyToOne (cascade = CascadeType.ALL)
    private City city;

}
