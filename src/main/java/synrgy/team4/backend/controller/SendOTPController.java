package synrgy.team4.backend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import synrgy.team4.backend.model.dto.request.OtpRequest;
import synrgy.team4.backend.model.dto.request.RegisterUserRequest;
import synrgy.team4.backend.model.dto.request.VerifyOtpRequest;
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
    public ResponseEntity<BaseResponse<Void>> sendOtp(@Valid @RequestBody OtpRequest request) {
        // Log detail request yang masuk
        System.out.println("Request diterima - Email: " + request.getEmail() + ", Nomor Telepon: " + request.getNoHP());

        try {
            otpService.sendOtp(request.getEmail(), request.getNoHP());

            // Simpan atau perbarui data pengguna sementara
            RegisterUserRequest registerUserRequest = new RegisterUserRequest();
            registerUserRequest.setEmail(request.getEmail());
            registerUserRequest.setNoHP(request.getNoHP());
            otpService.storeTemporaryUserData(registerUserRequest);

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
    public ResponseEntity<BaseResponse<Void>> verifyOtp(@RequestBody VerifyOtpRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();

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