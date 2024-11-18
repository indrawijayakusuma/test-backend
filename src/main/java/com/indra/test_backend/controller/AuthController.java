package com.indra.test_backend.controller;

import com.indra.test_backend.entity.User;
import com.indra.test_backend.entity.dto.UserCreateDto;
import com.indra.test_backend.service.JwtService;
import com.indra.test_backend.service.UserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;
    private final Bucket bucket;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    public AuthController() {
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserCreateDto userDto){
        if (bucket.tryConsume(1)){
            return ResponseEntity.ok().body(userService.create(userDto));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String>authenticateAndGetToken(@RequestBody UserCreateDto userDto){
        if (bucket.tryConsume(1)){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword()));
            if (authentication.isAuthenticated()){
                return ResponseEntity.ok().body(jwtService.generateToken(userDto.getUsername()));
            }else {
                throw new UsernameNotFoundException("not found");
            }
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
