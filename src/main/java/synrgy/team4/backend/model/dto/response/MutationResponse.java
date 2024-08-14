package synrgy.team4.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import synrgy.team4.backend.model.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private BigDecimal balance;
}
