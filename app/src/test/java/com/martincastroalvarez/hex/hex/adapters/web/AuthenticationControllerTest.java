package com.martincastroalvarez.hex.hex.adapters.web;

import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReset() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        ResponseEntity<?> responseEntity = authenticationController.reset(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(authenticationService, times(1)).recover(request.getEmail());
    }
}
