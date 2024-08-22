package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import synrgy.team4.backend.model.dto.request.FCMTokenRequest;
import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.dto.response.UserResponse;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.security.jwt.CustomUserDetails;
import synrgy.team4.backend.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public BaseResponse<UserResponse> authenticatedUser() {
        UserResponse userResponse = userService.getUserResponse();
        return BaseResponse.<UserResponse>builder()
                .success(true)
                .data(userResponse)
                .message("User data retrieved successfully.")
                .build();
    }

    @PutMapping("/fcm-token")
    public ResponseEntity<String> updateFCMToken(@RequestBody FCMTokenRequest fcmTokenRequest, Authentication authentication) {
        // Dapatkan CustomUserDetails dari Authentication
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // Dapatkan User dari CustomUserDetails
        User user = customUserDetails.getUser();

        // Update FCM token
        userService.updateFCMToken(user, fcmTokenRequest.getFcmToken());

        return ResponseEntity.ok("FCM Token updated successfully");
    }

}
