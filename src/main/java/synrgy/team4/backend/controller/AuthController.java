package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import synrgy.team4.backend.model.dto.LoginDTO;
import synrgy.team4.backend.model.dto.LoginResponseDTO;
import synrgy.team4.backend.model.dto.RegisterDTO;
import synrgy.team4.backend.model.dto.UserDTO;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.repository.UserRepository;
import synrgy.team4.backend.security.jwt.CustomUserDetails;
import synrgy.team4.backend.security.jwt.service.JwtService;
import synrgy.team4.backend.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body("{\"message\": \"Email already exist\"}");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setId(savedUser.getId());
        registerDTO.setName(savedUser.getName());
        registerDTO.setEmail(savedUser.getEmail());

        return ResponseEntity.status(201).body(registerDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());
        if (userOptional.isPresent() && encoder.matches(loginDTO.getPassword(), userOptional.get().getPassword())) {
            User user = userOptional.get();
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            String token = jwtService.generateToken(customUserDetails);

            return ResponseEntity.ok().body(new LoginResponseDTO(user.getId(), user.getName(), user.getEmail(), token));
        }

        return ResponseEntity.status(400).body("{\"message\": \"Invalid input email or password\"}");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
}
