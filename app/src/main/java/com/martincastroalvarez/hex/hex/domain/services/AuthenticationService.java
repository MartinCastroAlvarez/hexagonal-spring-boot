package com.martincastroalvarez.hex.hex.domain.services;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.User;

public interface AuthenticationService {
    String encodePassword(String password);
    String getToken(String email);
    User parseToken(String token) throws AuthenticationException;
    User signUp(String email, String password) throws HexagonalSecurityException;
    User login(String email, String password) throws HexagonalSecurityException;
    void recover(String email) throws UserNotFoundException;
}
