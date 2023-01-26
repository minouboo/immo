package studi.immo.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Address extends GenericEntity{



    private Long streetNumber;

    @Column
    private String streetName;

    @ManyToOne
    private City city;

}
