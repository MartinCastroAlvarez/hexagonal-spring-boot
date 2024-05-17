package com.martincastroalvarez.hex.hex.application;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.UserRepository;
import com.martincastroalvarez.hex.hex.domain.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import io.jsonwebtoken.Claims;
import java.util.Optional;
import java.util.Date;

@Service
public class AuthenticationApplication extends HexagonalApplication implements AuthenticationService {
  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.defaultPassword}")
  private String defaultPassword;

  @Value("${jwt.ttl}")
  private Integer tokenTtl;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Override
  public String getToken(String email) {
    long expirationTime = 1000 * tokenTtl;
    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS512)
        .compact();
  }

  @Override
  public User parseToken(String token) throws AuthenticationException {
    Claims claims = Jwts.parserBuilder()
      .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
      .build()
      .parseClaimsJws(token)
      .getBody();
    String email = claims.getSubject();
    User user = userRepository.get(email).orElseThrow(() -> new AuthenticationException());
    if (!user.getIsActive())
      throw new AuthenticationException();
    return user;
  }

  @Override
  public String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }

  @Override
  public User signUp(String email , String password) {
    logger.info(String.format("Signing up. Email: %s", email));
    if (email == null || email == "" || !email.contains("@") || !email.contains(".") || email.length() < 5 || email.length() > 50)
      throw new InvalidEmailException();
    if (password == null || password == "" || password.length() < 8 || password.length() > 50)
      throw new InvalidPasswordException();
    if (userRepository.get(email).isPresent())
      throw new EmailAlreadyTakenException();
    User user = new User();
    user.setName(email);
    user.setEmail(email);
    user.setPasswordHash(encodePassword(password));
    user.setRole(User.Role.USER);
    user.setIsActive(true);
    user.setSignUpDate(LocalDateTime.now());
    user.setLastLoginDate(user.getSignUpDate());
    return userRepository.save(user);
  }

  @Override
  public User login(String email, String password) {
    logger.info(String.format("Logging in. Email: %s", email));
    Optional<User> optionalUser = userRepository.get(email);
    if (!optionalUser.isPresent())
      throw new AuthenticationException();
    String encodedPassword = optionalUser.get().getPasswordHash();
    if (!passwordEncoder.matches(password, encodedPassword))
      throw new AuthenticationException();
    User user = optionalUser.get();
    if (!user.getIsActive())
      throw new UserDisabledException();
    user.setLastLoginDate(user.getSignUpDate());
    return userRepository.save(user);
  }

  @Override
  public void recover(String email) {
    logger.info(String.format("Recovering password. Email: %s", email));
    User user = userRepository.get(email).orElseThrow(() -> new UserNotFoundException());
    user.setPasswordHash(passwordEncoder.encode(defaultPassword));
    userRepository.save(user);
  }
}