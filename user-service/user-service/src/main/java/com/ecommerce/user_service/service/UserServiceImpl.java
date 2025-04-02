package com.ecommerce.user_service.service;

import com.ecommerce.user_service.exception.UserAlreadyExistsException;
import com.ecommerce.user_service.model.User;
import com.ecommerce.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository repo;
    @Autowired private PasswordEncoder encoder;

    @Override
    public User registerUser(User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public User getUserByUsername(String username) {

//        Optional<User> userOptional = repo.findByUsername(username);
        Optional<User> userOptional = repo.findByEmail(username);
         if (userOptional.isPresent()) {
            return userOptional.get();  // Return the user if found
        } else {
            // Handle case where the user is not found (throw an exception or return null)
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}