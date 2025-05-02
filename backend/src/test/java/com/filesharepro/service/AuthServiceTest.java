package com.filesharepro.service;

import com.filesharepro.config.TestConfig;
import com.filesharepro.dto.LoginRequest;
import com.filesharepro.dto.SignupRequest;
import com.filesharepro.entity.User;
import com.filesharepro.repository.UserRepository;
import com.filesharepro.security.JwtTokenProvider;
import com.filesharepro.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@SpringBootTest
@Import(TestConfig.class)
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SignupRequest validSignupRequest;
    private LoginRequest validLoginRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        validSignupRequest = new SignupRequest();
        validSignupRequest.setUsername("testUser");
        validSignupRequest.setEmail("test@example.com");
        validSignupRequest.setPassword("password123");

        validLoginRequest = new LoginRequest();
        validLoginRequest.setUsername("testUser");
        validLoginRequest.setPassword("password123");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword(passwordEncoder.encode("password123"));
    }

    @Test
    void registerUser_WithValidCredentials_ShouldSucceed() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(mockUser);

        // Mock the authentication process
        Authentication mockAuth = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "test@example.com", "password123");
        when(mockAuth.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
        when(tokenProvider.generateToken(any())).thenReturn("mock.jwt.token");

        var response = authService.registerUser(validSignupRequest);
        
        assertNotNull(response);
        assertEquals("testUser", response.getUsername());
        assertNotNull(response.getToken());
    }

    @Test
    void registerUser_WithExistingUsername_ShouldThrowException() {
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            authService.registerUser(validSignupRequest);
        });
    }

    @Test
    void registerUser_WithExistingEmail_ShouldThrowException() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            authService.registerUser(validSignupRequest);
        });
    }
}