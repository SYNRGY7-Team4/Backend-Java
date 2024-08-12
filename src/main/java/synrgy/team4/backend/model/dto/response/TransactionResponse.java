package synrgy.team4.backend.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TransactionResponse {
    private UUID id;
    private String accountFrom;
    private String nameAccountFrom;
    private String accountTo;
    private String nameAccountTo;
    private BigDecimal amount;
    private LocalDateTime datetime;
    private String type;
    private String status;
    private String description;
    private BigDecimal balance;
}
