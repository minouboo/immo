package studi.immo.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
public class City extends GenericEntity{

    @Column
    private String name;

    @Column
    private Long zipCode;

}
