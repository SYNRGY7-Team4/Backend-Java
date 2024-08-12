package synrgy.team4.backend.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SetBalanceRequest {
    private String accountNumber;
    private BigDecimal newBalance;
}
