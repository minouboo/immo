package studi.immo.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class PaymentRequest extends GenericEntity{

    @Column
    private Boolean tenantPaid;

    @ManyToOne
    private Agreement agreement;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private LocalDateTime issueDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private LocalDateTime paymentDate;

    @Column
    private BigDecimal rentalPrice;

    @Column
    private BigDecimal charges;

    @Column
    private BigDecimal otherAmount;

    @Column
    private BigDecimal totalAmount;

    @Column
    private PaymentType paymentType;

    @Column
    private String comments;

    /*@ElementCollection (targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable (name="payment_type")
    @Column (name="type")
    @Enumerated (value = EnumType.STRING)
    private Set<PaymentType> paymentType = new HashSet<>();*/





}
