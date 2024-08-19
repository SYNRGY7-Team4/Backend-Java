package synrgy.team4.backend.service;

import synrgy.team4.backend.model.dto.request.RegisterUserRequest;

public interface OtpService {
    String generateOtp();
    void storeTemporaryData(String email, String otp);
    boolean verifyOtp(String email, String otp);
    boolean isEmailRegistered(String email);
    String sendOtp(String email, String noHP);
    RegisterUserRequest getTemporaryUserData(String email);
    void storeTemporaryUserData(RegisterUserRequest request);
    public void removeTemporaryUserData(String email);
}
