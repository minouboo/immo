package studi.immo.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class AgreementForm {

    private static final long serialVersionUID = 1L;

    private BigDecimal rentalPrice;

    private BigDecimal charges;

    private BigDecimal deposit;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startAgreementDate;

    private BigDecimal agencyFees;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startInventoryDate;

    private String comments;

    private Long tenantUserId;

}
