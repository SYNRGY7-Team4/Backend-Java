package synrgy.team4.backend.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TransactionResponse {
    private LocalDateTime createdAt;
    private UUID id;
    private String referenceNumber;
    private String accountFrom;
    private String accountFromType;
    private String nameAccountFrom;
    private String accountTo;
    private String accountToType;
    private String nameAccountTo;
    private String destinationBank;
    private BigDecimal amount;
    private LocalDateTime datetime;
    private String type;
    private String status;
    private String description;
    private BigDecimal currentBalance;
}
