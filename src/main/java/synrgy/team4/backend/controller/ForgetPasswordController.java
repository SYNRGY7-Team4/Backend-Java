package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import synrgy.team4.backend.config.EmailConfig;
import synrgy.team4.backend.model.dto.request.ResetPasswordModel;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.repository.UserRepository;
import synrgy.team4.backend.service.UserService;
import synrgy.team4.backend.service.impl.EmailSender;
import synrgy.team4.backend.utils.EmailTemplate;
import synrgy.team4.backend.utils.Response;
import synrgy.team4.backend.utils.SimpleStringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/forget-password/")
public class ForgetPasswordController {

    @Autowired
    private UserRepository userRepository;

    EmailConfig emailConfig = new EmailConfig();

    @Autowired
    public UserService serviceReq;

    @Value("${expired.token.password.minute:30}")
    private int expiredToken;

    @Autowired
    public Response templateEror;

    @Autowired
    public EmailTemplate emailTemplate;

    @Autowired
    public EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Step 1 : Send OTP
    @PostMapping("/send")//send OTP//send OTP
    public Map sendEmailPassword(@RequestBody ResetPasswordModel user) {
        String message = "Thanks, please check your email";

        if (StringUtils.isEmpty(user.getEmail())) return templateEror.templateEror("No email provided");
        User found = userRepository.findOneByUsername(user.getEmail());
        if (found == null) return templateEror.templateEror("Email not found"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getResetPassword();
        if (StringUtils.isEmpty(found.getOtp())) {
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
        emailSender.sendAsync(found.getUsername(), "Chute - Forget Password", template);


        return templateEror.templateSuksess("success");

    }

    //Step 2 : CHek TOKEN OTP EMAIL
    @PostMapping("/validate")
    public Map cheKTOkenValid(@RequestBody ResetPasswordModel model) {
        if (model.getOtp() == null) return templateEror.templateEror("Token is required");

        User user = userRepository.findOneByOTP(model.getOtp());
        if (user == null) {
            return templateEror.templateEror("Token not valid");
        }

        return templateEror.templateSuksess("Success");
    }

    // Step 3 : lakukan reset password baru
    @PostMapping("/change-password")
    public Map resetPassword(@RequestBody ResetPasswordModel model) {
        if (model.getOtp() == null) return templateEror.templateEror("Token is required");
        if (model.getNewPassword() == null) return templateEror.templateEror("New Password is required");
        User user = userRepository.findOneByOTP(model.getOtp());
        String success;
        if (user == null) return templateEror.templateEror("Token not valid");

        user.setPassword(passwordEncoder.encode(model.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);

        try {
            userRepository.save(user);
            success = "success";
        } catch (Exception e) {
            return templateEror.templateEror("Gagal simpan user");
        }
        return templateEror.templateSuksess(success);
    }
}
