package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.dto.response.UserResponse;
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
                .build();
    }

}
