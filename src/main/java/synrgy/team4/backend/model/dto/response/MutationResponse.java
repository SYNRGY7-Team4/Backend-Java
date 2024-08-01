package synrgy.team4.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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

    @JsonProperty("account_to")
    private String accountTo;

    private BigDecimal amount;
    private LocalDateTime datetime;
    private String type; // e.g., "deposit", "withdrawal", "transfer"
    private String status;
    private String description;
}
