package studi.immo.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Setter
@Getter
@Entity
@PrimaryKeyJoinColumn( name = "userId")
public class Agency extends User{

    @Column
    private String agencyName;
}
