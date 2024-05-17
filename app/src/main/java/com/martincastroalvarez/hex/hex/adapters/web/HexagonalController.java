package com.martincastroalvarez.hex.hex.adapters.web;

import com.martincastroalvarez.hex.hex.domain.ports.out.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.exceptions.HexagonalException;
import com.martincastroalvarez.hex.hex.domain.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.MDC;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

public class HexagonalController {
    protected static final Logger logger = Logger.getLogger(HexagonalController.class.getName());
    
    @Autowired
    private UserRepository userRepository;

    protected User getAuthenticatedUser() {
        logger.info("Getting user from authentication");
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.get(principal).orElseThrow(() -> new UserNotFoundException());
        logger.info(String.format("User found: %s", user));
        return user;
    }

    @ExceptionHandler(HexagonalException.class)
    public ResponseEntity<Map<String, Object>> handleHexagonalException(HexagonalException ex) {
        logger.severe(String.format("Hexagonal exception: %s %s", ex.getClass().getSimpleName(), ex.getMessage()));
        Map<String, Object> response = new HashMap<>();
        response.put("Timestamp", System.currentTimeMillis());
        response.put("Error", ex.getClass().getSimpleName());
        response.put("Message", ex.getMessage());
        response.put("RequestId", MDC.get("RequestId"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
