package studi.immo.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccommodationForm {

    private static final long serialVersionUID = 1L;

    private String title;

    private Double squareMeter;

    private Integer rooms;

    private String description;

    private BigDecimal rentalPrice;

    private BigDecimal charges;

    private BigDecimal deposit;

    private BigDecimal agencyFees;

    private Long userId;

    private Long addressId;


}
