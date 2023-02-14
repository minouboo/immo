package studi.immo.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
public class Advertisement extends GenericEntity{

    @Column
    private String title;

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


}
