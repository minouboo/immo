package studi.immo.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Setter
@Getter
@Entity
public class Agency extends GenericEntity{

    @Column
    private String agencyName;

    @OneToOne
    private User user;

}
