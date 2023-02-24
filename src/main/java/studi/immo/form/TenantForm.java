package studi.immo.form;

import lombok.Getter;
import lombok.Setter;
import studi.immo.entity.User;

import java.math.BigDecimal;

@Getter
@Setter
public class TenantForm {

    private static final long serialVersionUID = 1L;

    private BigDecimal revenues;

    private User userId;
}
