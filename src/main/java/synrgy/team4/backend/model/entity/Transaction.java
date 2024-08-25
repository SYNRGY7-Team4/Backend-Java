package synrgy.team4.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_from", referencedColumnName = "account_number")
    private Account accountFrom;

    private String accountFromType;

    @ManyToOne
    @JoinColumn(name = "account_to", referencedColumnName = "account_number")
    private Account accountTo;

    private String accountToType;

    private BigDecimal amount;
    private LocalDateTime datetime;
    private LocalDateTime createdAt;
    private String type;
    private String status;
    private String description;
    private String referenceNumber;
    private String destinationBank;
    private BigDecimal currentBalance;
}
