package synrgy.team4.backend.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    private String accountFrom;
    private String accountTo;
    private BigDecimal amount;
    private String description;
}
