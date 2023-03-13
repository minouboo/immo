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

    @Column
    private String situationFamiliale;

    @Column
    private String situationProfessionelle;

    @ManyToOne
    private Agreement agreement;

    @OneToOne
    private User user;

}
