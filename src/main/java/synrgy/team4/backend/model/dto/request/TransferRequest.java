package synrgy.team4.backend.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransferRequest {
    private String accountFrom;
    private String accountTo;
    private BigDecimal amount;
    private String description;
    private String pin;
    private LocalDateTime datetime;
    private String destinationBank;
}
