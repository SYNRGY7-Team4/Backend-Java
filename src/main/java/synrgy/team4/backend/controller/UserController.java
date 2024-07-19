package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import synrgy.team4.backend.model.dto.ApiResponse;
import synrgy.team4.backend.model.dto.response.UserResponse;
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
    public ApiResponse<UserResponse> authenticatedUser() {
        UserResponse userResponse = userService.authenticatedUser();
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .data(userResponse)
                .build();
    }
}
