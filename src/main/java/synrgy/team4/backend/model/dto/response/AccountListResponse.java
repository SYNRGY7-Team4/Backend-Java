package synrgy.team4.backend.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AccountListResponse {
    private UUID id;
    private String name;
    private String accountNumber;
    private String destinationBank;
}
