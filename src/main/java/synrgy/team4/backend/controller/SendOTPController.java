package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import synrgy.team4.backend.model.dto.request.RegisterUserRequest;
import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.service.AuthService;
import synrgy.team4.backend.service.OtpService;

import java.util.Map;

@RestController
public class SendOTPController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<BaseResponse<Void>> sendOtp(@RequestBody RegisterUserRequest request) {
        try {
            // Memastikan email belum terdaftar
            if (otpService.isEmailRegistered(request.getEmail())) {
                return new ResponseEntity<>(BaseResponse.<Void>builder()
                        .success(false)
                        .message("Email sudah terdaftar.")
                        .build(), HttpStatus.BAD_REQUEST);
            }
            otpService.sendOtp(request.getEmail(), request.getNoHP());
            otpService.storeTemporaryUserData(request);

            return new ResponseEntity<>(BaseResponse.<Void>builder()
                    .success(true)
                    .message("OTP berhasil dikirim. Silakan periksa email Anda.")
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(BaseResponse.<Void>builder()
                    .success(false)
                    .message("Gagal mengirim OTP: " + e.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<BaseResponse<Void>> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        try {
            boolean isOtpValid = otpService.verifyOtp(email, otp);
            if (!isOtpValid) {
                return new ResponseEntity<>(BaseResponse.<Void>builder()
                        .success(false)
                        .message("OTP tidak valid.")
                        .build(), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(BaseResponse.<Void>builder()
                    .success(true)
                    .message("OTP berhasil diverifikasi.")
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(BaseResponse.<Void>builder()
                    .success(false)
                    .message("Verifikasi OTP gagal: " + e.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}