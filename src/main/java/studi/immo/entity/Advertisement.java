package studi.immo.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Setter
@Getter
public class Advertisement extends GenericEntity{

    @Column
    private String description;

    @Column
    private BigDecimal rentalPrice;

    @Column
    private BigDecimal charges;

    @Column
    private BigDecimal deposit;

    @Column
    private BigDecimal agencyFees;

    @ManyToOne
    private Accommodation accommodation;

    @OneToMany (mappedBy = "advertisement", cascade = CascadeType.REMOVE)
    private List<Photo> photos;

    /*@Transient
    public String getPhotoPath (){
        if (photos == null )
        {
            return null;
        }

    }*/


}
