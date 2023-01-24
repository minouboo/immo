package studi.immo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
public class Cash extends GenericEntity {

    @Column
    private BigDecimal amount;

    @OneToOne
    private Tenant tenant;

}
