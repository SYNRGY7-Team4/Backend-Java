package synrgy.team4.backend.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AccountResponse {
    private UUID id;
    private String accountNumber;
    private BigDecimal balance;
    private UUID userId;
    private String userName;
}
