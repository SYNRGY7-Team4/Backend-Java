package synrgy.team4.backend.service;

import java.math.BigDecimal;

public interface BalanceService {
    BigDecimal getBalance(String accountNumber);
}
