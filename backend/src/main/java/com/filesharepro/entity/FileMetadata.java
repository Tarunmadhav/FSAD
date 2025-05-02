package com.filesharepro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Filename is required")
    @Size(max = 255, message = "Filename must not exceed 255 characters")
    @Column(nullable = false)
    private String filename;

    @NotBlank(message = "Original filename is required")
    @Size(max = 255, message = "Original filename must not exceed 255 characters")
    @Column(nullable = false)
    private String originalFilename;

    @NotBlank(message = "Content type is required")
    @Column(nullable = false)
    private String contentType;

    @NotNull(message = "File size is required")
    private Long size;

    @NotNull(message = "Upload date is required")
    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    private boolean isPublic = false;

    @NotBlank(message = "File key is required")
    @Column(unique = true, nullable = false)
    private String fileKey;

    private LocalDateTime expiryDate;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SharingKey> sharingKeys = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
}