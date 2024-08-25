package synrgy.team4.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import synrgy.team4.backend.model.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MutationResponse {
    private UUID id;

    @JsonProperty("account_from")
    private String accountFrom;
    @JsonProperty("username_from")
    private String nameAccountFrom;

    @JsonProperty("account_to")
    private String accountTo;
    @JsonProperty("username_to")
    private String nameAccountTo;

    private BigDecimal amount;
    private LocalDateTime datetime;
    private String type; // e.g., "deposit", "withdrawal", "transfer"
    private String status;
    private String description;
    private BigDecimal currentBalance;

    public MutationResponse(UUID id, String accountNumber, String name, String accountNumber1, String name1, BigDecimal amount, LocalDateTime datetime, String type, String status, String description, AtomicReference<BigDecimal> currentBalance) {
    }
}
