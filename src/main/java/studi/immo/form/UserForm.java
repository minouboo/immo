package studi.immo.form;

import lombok.Getter;
import lombok.Setter;
import studi.immo.entity.User;

import java.math.BigDecimal;

@Getter
@Setter
public class UserForm {

    private static final long serialVersionUID = 1L;

    private BigDecimal revenues;

    private User userId;

    private String agencyName;

    private Long streetNumber;

    private String streetName;

    private String cityName;

    private Long zipCode;

}
