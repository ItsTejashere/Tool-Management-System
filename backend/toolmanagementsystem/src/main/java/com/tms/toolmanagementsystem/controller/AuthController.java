package com.tms.toolmanagementsystem.controller;

import com.tms.toolmanagementsystem.entity.User;
import com.tms.toolmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.tms.toolmanagementsystem.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // 🚀 NEW: Spring's built-in email sender
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    // Temporary storage for OTPs.
    private Map<String, String> otpStorage = new HashMap<>();

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User dbUser = userRepository.findByUsername(loginRequest.getUsername());

        // Verify password using BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (dbUser != null && encoder.matches(loginRequest.getPassword(), dbUser.getPassword())) {

            // Generate JWT token
            String token = jwtUtil.generateToken(dbUser);

            Map<String, Object> resp = new HashMap<>();
            resp.put("status", true);
            resp.put("message", "Login Successful");
            resp.put("token", token);
            resp.put("role", dbUser.getRole());
            resp.put("plantId", dbUser.getPlantId() != null ? dbUser.getPlantId() : "null");
            resp.put("deptId", dbUser.getDeptId() != null ? dbUser.getDeptId() : "null");

            return ResponseEntity.ok(resp);
        }

        return ResponseEntity.status(401).body("{\"status\": false, \"message\": \"Invalid Credentials\"}");
    }

    // 🚀 STEP 1: Generate OTP and send via EMAIL
    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestOtp(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        User dbUser = userRepository.findByUsername(username);

        if (dbUser != null && dbUser.getEmail() != null && !dbUser.getEmail().isEmpty()) {

            // Generate 4-digit OTP
            String otp = String.format("%04d", new Random().nextInt(10000));
            otpStorage.put(username, otp);

            try {
                // 🚀 Fire the Email!
                SimpleMailMessage message = new SimpleMailMessage();

                message.setFrom(senderEmail); // 🚀 ADD THIS LINE!

                message.setTo(dbUser.getEmail());
                message.setSubject("TMS Password Reset OTP");
                message.setText("Hello " + username + ",\n\nYour OTP to reset your Tool Management System password is: " + otp + "\n\nIf you did not request this, please ignore this email.");

                mailSender.send(message);

                return ResponseEntity.ok("{\"status\": true, \"message\": \"OTP sent to registered email address.\"}");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("{\"status\": false, \"message\": \"Failed to send Email. Server error.\"}");
            }
        }
        return ResponseEntity.badRequest().body("{\"status\": false, \"message\": \"Username not found or no email registered.\"}");
    }

    // 🚀 STEP 2: Verify the typed OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String userOtp = request.get("otp");

        if (otpStorage.containsKey(username) && otpStorage.get(username).equals(userOtp)) {
            otpStorage.remove(username); // Clear it so it can't be reused
            return ResponseEntity.ok("{\"status\": true, \"message\": \"OTP Verified.\"}");
        }
        return ResponseEntity.status(401).body("{\"status\": false, \"message\": \"Invalid or Expired OTP.\"}");
    }

    // 🚀 STEP 3: Save the newly created password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String newPassword = request.get("newPassword");

        boolean isUpdated = userRepository.updateUserPassword(username, newPassword);

        if (isUpdated) {
            return ResponseEntity.ok("{\"status\": true, \"message\": \"Password changed successfully. Please log in.\"}");
        }
        return ResponseEntity.status(500).body("{\"status\": false, \"message\": \"Failed to update password.\"}");
    }
}
