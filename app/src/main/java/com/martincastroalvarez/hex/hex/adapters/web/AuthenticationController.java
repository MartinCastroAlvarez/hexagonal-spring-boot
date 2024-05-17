package com.martincastroalvarez.hex.hex.adapters.web;

import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationController extends HexagonalController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        logger.info(String.format("Logging in with email=%s", request.getEmail()));
        User user = authenticationService.login(request.getEmail(), request.getPassword());
        String jwt = authenticationService.getToken(user.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("Token", jwt);
        response.put("userId", user.getId());
        response.put("userRole", user.getRole().name());
        response.put("userName", user.getUsername());
        logger.info(String.format("Logged in with email=%s", request.getEmail()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signUp(@RequestBody AuthenticationRequest request) {
        logger.info(String.format("Signing up with email=%s", request.getEmail()));
        User user = authenticationService.signUp(request.getEmail(), request.getPassword());
        String jwt = authenticationService.getToken(user.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("Token", jwt);
        response.put("userId", user.getId());
        response.put("userRole", user.getRole().name());
        response.put("userName", user.getUsername());
        logger.info(String.format("Signed up with email=%s", request.getEmail()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/reset")
    public ResponseEntity<?> reset(@RequestBody AuthenticationRequest request) {
        logger.info(String.format("Resetting password with email=%s", request.getEmail()));
        authenticationService.recover(request.getEmail());
        logger.info(String.format("Password reset with email=%s", request.getEmail()));
        return ResponseEntity.ok(null);
    }
}

class AuthenticationRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
