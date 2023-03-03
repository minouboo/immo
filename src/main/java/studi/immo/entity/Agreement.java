package studi.immo.entity;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private Date entryDate;

    @ManyToMany
    @JoinTable(
            name = "agreement_user",
            joinColumns = @JoinColumn (name = "agreement_id"),
            inverseJoinColumns = @JoinColumn (name = "user_id"))
    private Set<User> users = new HashSet<>();

    @ManyToOne
    private Accommodation accommodation;

    @Column (columnDefinition = "boolean default false")
    private Boolean tenantValidate = false;

    @Column (columnDefinition = "boolean default false")
    private Boolean LandlordValidate = false;



}
