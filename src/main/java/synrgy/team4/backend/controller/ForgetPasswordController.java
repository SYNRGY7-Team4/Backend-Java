package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.config.EmailConfig;
import synrgy.team4.backend.model.dto.request.ResetPasswordModel;
import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.repository.UserRepository;
import synrgy.team4.backend.service.impl.EmailSender;
import synrgy.team4.backend.utils.EmailTemplate;
import synrgy.team4.backend.utils.SimpleStringUtils;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/forget-password")
public class ForgetPasswordController {

    private final UserRepository userRepository;

    @Value("${expired.token.password.minute:30}")
    private int expiredToken;

    private final EmailTemplate emailTemplate;

    private final EmailSender emailSender;

    private final PasswordEncoder passwordEncoder;

    public ForgetPasswordController(
            UserRepository userRepository,
            EmailTemplate emailTemplate,
            EmailSender emailSender,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.emailTemplate = emailTemplate;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }

    // Step 1 : Send OTP
    @PostMapping("/send")//send OTP//send OTP
    public ResponseEntity<BaseResponse<String>> sendEmailPassword(@RequestBody ResetPasswordModel user) {
        if (user.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No email provided");
        }
        User found = userRepository.findOneByUsername(user.getEmail());
        if (found == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found with email " + user.getEmail());

        String template = emailTemplate.getResetPassword();
        if (found.getOtp() == null) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtils.randomString(6, true);
                search = userRepository.findOneByOTP(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();

            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", otp);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? "" +
                    "@UserName"
                    :
                    "@" + found.getUsername()));

            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? "" +
                    "@UserName"
                    :
                    "@" + found.getUsername()));
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", found.getOtp());
        }
        emailSender.sendAsync(found.getUsername(), "Lumi - Forget Password", template);


        BaseResponse<String> response = BaseResponse.<String>builder()
                .success(true)
                .message("OTP has been sent to your email")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Step 2 : CHek TOKEN OTP EMAIL
    @PostMapping("/validate")
    public ResponseEntity<BaseResponse<String>> cheKTokenValid(@RequestBody ResetPasswordModel model) {
        if (model.getOtp() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is required");

        User user = userRepository.findOneByOTP(model.getOtp());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token not valid");
        }

        BaseResponse<String> response = BaseResponse.<String>builder()
                .success(true)
                .message("Token valid")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Step 3 : lakukan reset password baru
    @PostMapping("/change-password")
    public ResponseEntity<BaseResponse<String>> resetPassword(@RequestBody ResetPasswordModel model) {
        if (model.getOtp() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is required");
        if (model.getNewPassword() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password is required");
        User user = userRepository.findOneByOTP(model.getOtp());
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token not valid");

        user.setPassword(passwordEncoder.encode(model.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to reset password");
        }

        BaseResponse<String> response = BaseResponse.<String>builder()
                .success(true)
                .message("Change password successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
