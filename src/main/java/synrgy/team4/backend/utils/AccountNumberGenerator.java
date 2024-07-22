package synrgy.team4.backend.utils;

import java.util.Random;

public class AccountNumberGenerator {
    private static final int ACCOUNT_NUMBER_LENGTH = 10;

    public static String generateAccountNumber() {
        StringBuilder accountNumberBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            accountNumberBuilder.append(random.nextInt(10));
        }

        return accountNumberBuilder.toString();
    }
}
