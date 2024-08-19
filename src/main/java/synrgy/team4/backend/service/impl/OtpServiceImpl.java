
package synrgy.team4.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import synrgy.team4.backend.model.dto.request.RegisterUserRequest;
import synrgy.team4.backend.service.OtpService;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class OtpServiceImpl implements OtpService {
    private final ConcurrentMap<String, String> otpStorage = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, RegisterUserRequest> temporaryUserDataStorage = new ConcurrentHashMap<>();

    @Autowired
    private EmailSender emailSender;

    @Override
    public String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // 6-digit OTP
    }

    @Override
    public void storeTemporaryData(String email, String otp) {
        otpStorage.put(email, otp);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStorage.get(email);

        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(email); // Hapus OTP setelah diverifikasi

            // Update status verifikasi di data pengguna sementara
            RegisterUserRequest tempRequest = temporaryUserDataStorage.get(email);
            if (tempRequest != null) {
                tempRequest.setVerified(true);  // Tandai sebagai tervalidasi
                temporaryUserDataStorage.put(email, tempRequest); // Update peta
            }

            return true;
        }
        return false;
    }



    @Override
    public boolean isEmailRegistered(String email) {
        return temporaryUserDataStorage.containsKey(email);
    }

    @Override
    public String sendOtp(String email, String noHP) {
        String otp = generateOtp();
        emailSender.send(email, "OTP for Verification", "Your OTP is: " + otp);
        storeTemporaryData(email, otp);
        return otp;
    }

    @Override
    public RegisterUserRequest getTemporaryUserData(String email) {
        return temporaryUserDataStorage.get(email);
    }

    @Override
    public void storeTemporaryUserData(RegisterUserRequest request) {
        temporaryUserDataStorage.put(request.getEmail(), request);
    }

    @Override
    public void removeTemporaryUserData(String email) {
        temporaryUserDataStorage.remove(email);
    }
}
