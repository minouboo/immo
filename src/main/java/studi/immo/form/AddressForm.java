package studi.immo.form;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressForm {

    private static final long serialVersionUID = 1L;

    private Long streetNumber;

    private String streetName;

    private String cityName;

    private Long zipCode;


}
