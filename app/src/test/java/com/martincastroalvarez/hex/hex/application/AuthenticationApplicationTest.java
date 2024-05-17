package com.martincastroalvarez.hex.hex.application;

import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationApplicationTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationApplication authenticationApplication;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(authenticationApplication, "secretKey", "abaksdlfjhalsdhjfakjsdhfkhjadskjfhalkjsdhfkajhsdfkjhasdlkfjhasdkjhfalkjsdhfklajhsdfkljahsdfkljhasdlkjfhaksjdhfajsdfhlasjdf");
        ReflectionTestUtils.setField(authenticationApplication, "defaultPassword", "123");
        ReflectionTestUtils.setField(authenticationApplication, "tokenTtl", 1000);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    public AuthenticationApplicationTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEncodePassword() {
        String password = "password";
        String encodedPassword = authenticationApplication.encodePassword(password);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    @Test
    void testGetToken() {
        String token = authenticationApplication.getToken("test@example.com");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testLogin() {
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(encodedPassword);
        user.setIsActive(true);
        when(userRepository.get(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        User loggedInUser = authenticationApplication.login(email, password);
        assertNotNull(loggedInUser);
        assertEquals(email, loggedInUser.getEmail());
        assertTrue(loggedInUser.getIsActive());
        verify(userRepository).get(email);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    void testParseToken() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setIsActive(true);
        when(userRepository.get(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        String token = authenticationApplication.getToken(email);
        User parsedUser = authenticationApplication.parseToken(token);
        assertNotNull(parsedUser);
        assertEquals(user.getEmail(), parsedUser.getEmail());
        assertTrue(parsedUser.getIsActive());
    }

    @Test
    void testRecover() {
        String email = "test@example.com";
        User user = new User();
        when(passwordEncoder.encode("123")).thenReturn("abc");
        when(passwordEncoder.matches("123", "abc")).thenReturn(true);
        user.setIsActive(true);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode("123"));
        when(userRepository.get(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        authenticationApplication.recover(email);
        User recoveredUser = authenticationApplication.login(email, "123");
        assertNotNull(recoveredUser);
        assertTrue(recoveredUser.getIsActive());
    }

    @Test
    void testSignUp() {
        String email = "test2@example.com";
        String password = "password";
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setIsActive(true);
            return userToSave;
        });
        User user = authenticationApplication.signUp(email, password);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertTrue(user.getIsActive());
    }
}
