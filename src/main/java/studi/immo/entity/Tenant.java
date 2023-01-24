package studi.immo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@PrimaryKeyJoinColumn( name = "userId")
public class Tenant extends User {

    @Column
    private BigDecimal revenues;

    @ManyToOne
    private Agreement agreement;

    @OneToOne
    private Cash cash;

}
