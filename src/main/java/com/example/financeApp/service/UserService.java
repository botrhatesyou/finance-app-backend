package com.example.financeApp.service;

import com.example.financeApp.model.User;
import com.example.financeApp.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    private static final String jwtSecret = System.getenv("JWT_SECRET_KEY");
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {
        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to the database
        User savedUser = userRepository.save(user);

        // Generate confirmation token
        String confirmationToken = generateConfirmationToken(savedUser.getId());

        // Send confirmation email
        emailService.sendConfirmationEmail(savedUser, confirmationToken);

        return savedUser;
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword()) && user.getIsVerified()) {
            return generateJwtToken(user.getId());
        }
        return null;
    }

    public boolean confirmUser(String token) {
        // Extract user ID from the token and confirm the user
        UUID userId = UUID.fromString(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject());
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setIsVerified(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private String generateJwtToken(UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userId.toString());
        claims.put("created", new Date());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 1 week
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private String generateConfirmationToken(UUID userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public static UUID getUserIdFromToken(String token) {
        try {
            // Remove the "Bearer " prefix
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            System.out.println("Token received: " + token);  // Debug log
            String userId = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
            return UUID.fromString(userId);
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return null;
    }


}

