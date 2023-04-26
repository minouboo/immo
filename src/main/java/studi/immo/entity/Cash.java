package studi.immo.entity;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
public class Cash extends GenericEntity {

    @Column
    private BigDecimal amount;

    @OneToOne (cascade = CascadeType.REMOVE)
    private User user;

}
