package studi.immo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
public class Address extends GenericEntity{


    @Column
    private Long streetNumber;

    @Column
    private String streetName;

    @ManyToOne
    private City city;

}
