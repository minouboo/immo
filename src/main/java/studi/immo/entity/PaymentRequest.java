package studi.immo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class PaymentRequest extends GenericEntity{

    @ManyToOne
    private Cash cash;

    @Column
    private Boolean tenantPaid;

    @ManyToOne
    private Agreement agreement;

    @Column
    @Enumerated (value = EnumType.STRING)
    private PaymentType paymentType;


}
