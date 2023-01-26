package studi.immo.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Setter
@Getter
public class Agreement extends GenericEntity{

    @Column
    private BigDecimal rentalPrice;

    @Column
    private BigDecimal charges;

    @Column
    private BigDecimal deposit;

    @Column
    private BigDecimal agencyFees;

    @Column
    private Date entryDate;

    @ManyToOne
    private Accommodation accomodation;



}
