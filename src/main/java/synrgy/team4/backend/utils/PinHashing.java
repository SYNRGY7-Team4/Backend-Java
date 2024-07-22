package synrgy.team4.backend.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PinHashing {
    public static String hashPin(String pin) {
        return BCrypt.gensalt(12) + BCrypt.hashpw(pin, BCrypt.gensalt(12));
    }
}
