package studi.immo.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class Agency extends GenericEntity{

    @Column
    private String agencyName;

    @OneToOne (cascade = CascadeType.REMOVE)
    private User user;

}
