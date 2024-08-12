package synrgy.team4.backend.utils;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PinHashing {
    public static String hashPin(String pin) {
        return BCrypt.gensalt(12) + BCrypt.hashpw(pin, BCrypt.gensalt(12));
    }

    public static boolean verifyPin(String enteredPin, String storedPin) {
        return BCrypt.checkpw(enteredPin, storedPin.substring(29));
    }
}
