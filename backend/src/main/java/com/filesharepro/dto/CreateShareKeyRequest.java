package com.filesharepro.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateShareKeyRequest {
    @NotNull(message = "File ID is required")
    private Long fileId;

    @Future(message = "Expiry date must be in the future")
    private LocalDateTime expiresAt;

    @Min(value = 1, message = "Maximum uses must be at least 1")
    private Integer maxUses;
}