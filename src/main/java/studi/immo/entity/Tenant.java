package studi.immo.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
public class Tenant extends GenericEntity{

    @Column
    private BigDecimal revenues;

    @ManyToOne
    private Agreement agreement;

    @OneToOne
    private User user;

}
