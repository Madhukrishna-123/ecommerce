package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.jwt.JwtUtil;
import com.ecommerce.user_service.model.*;
import com.ecommerce.user_service.service.ForgotPasswordService;
import com.ecommerce.user_service.service.UserDetailsServiceImpl;
import com.ecommerce.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AuthenticationManager authManager;
    @Autowired  private  ForgotPasswordService forgotPasswordService;



//    public AuthController(ForgotPasswordService forgotPasswordService) {
//        this.forgotPasswordService = forgotPasswordService;
//    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        System.out.println(email);

        if (!userDetailsService.userExists(email)) {
            return ResponseEntity.badRequest().body("Invalid email! User not found.");
        }

        return ResponseEntity.ok(forgotPasswordService.sendOtp(email));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        boolean isValid = forgotPasswordService.verifyOtp(email, otp);
        return isValid ? ResponseEntity.ok("OTP Verified!") : ResponseEntity.badRequest().body("Invalid OTP");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        boolean resetSuccessful = forgotPasswordService.resetPassword(email, otp, newPassword);
        return resetSuccessful ? ResponseEntity.ok("Password successfully reset!") :
                ResponseEntity.badRequest().body("Invalid OTP or email!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // Remove "Bearer " prefix to get the token
        }
        return null;  // If no token is found
    }
    @GetMapping("/userdetails")
    public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
        try {
            // Correctly extract the JWT token from the request
            String token = extractTokenFromRequest(request);
            System.out.println("USER TOKEN: " + token);

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing");
            }

            // Extract the username (email) from the token
            String username = jwtUtil.extractUsername(token);
            System.out.println("Extracted Username: " + username);

            // Validate the token with the extracted username (email)
            if (username == null || !jwtUtil.validateToken(token, userDetailsService.loadUserByUsername(username))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            // Load the full user data from your user service based on the username (which is the email)
            User user = userService.getUserByUsername(username);  // Get the user by email (which is username in the token)

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Print the full user data to the console (as a JSON-like string)
            System.out.println("User Details: " + user);

            // Return the full user data as a response (this will return the user object in JSON format)
            return ResponseEntity.ok(user);  // This will return the user with "username" as "john_doe"
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching user details");
        }
    }


}
