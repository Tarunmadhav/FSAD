package com.filesharepro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Username can only contain letters, numbers, underscores and hyphens")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Profile name is required")
    @Size(min = 3, max = 50, message = "Profile name must be between 3 and 50 characters")
    @Pattern(regexp = "^[\\p{L}\\s'-]+$", message = "Profile name can only contain letters, spaces, hyphens and apostrophes")
    private String profileName;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @Size(max = 1000, message = "Profile picture URL must not exceed 1000 characters")
    @Column(length = 1000)
    private String profilePicture;

    @NotNull(message = "Storage used value is required")
    @Min(value = 0, message = "Storage used must not be negative")
    private Long storageUsed = 0L;

    @NotNull(message = "Storage limit value is required")
    @Min(value = 1024 * 1024, message = "Storage limit must be at least 1MB")
    private Long storageLimit = 1073741824L; // 1GB default

    @NotNull(message = "Creation date is required")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileMetadata> files = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}