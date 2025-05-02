package com.filesharepro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sharing_keys")
public class SharingKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Key is required")
    @Size(min = 6, max = 6, message = "Key must be exactly 6 characters")
    @Column(nullable = false, unique = true)
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileMetadata file;

    @NotNull(message = "Creation date is required")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @Min(value = 1, message = "Maximum uses must be at least 1")
    private Integer maxUses;
    
    @NotNull(message = "Usage count is required")
    @Min(value = 0, message = "Usage count must not be negative")
    private Integer usageCount = 0;

    @NotNull(message = "Active status is required")
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}