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

    @ManyToOne
    @JoinColumn(name = "account_to", referencedColumnName = "account_number")
    private Account accountTo;

    private BigDecimal amount;
    private LocalDateTime datetime;
    private String type; // e.g., "deposit", "withdrawal", "transfer"
    private String status;
    private String description;
}
