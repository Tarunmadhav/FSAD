package com.filesharepro.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileDto {
    @Size(min = 3, max = 50, message = "Profile name must be between 3 and 50 characters")
    @Pattern(regexp = "^[\\p{L}\\s'-]+$", message = "Profile name can only contain letters, spaces, hyphens and apostrophes")
    private String profileName;

    @Pattern(regexp = "^(data:image/(jpeg|png|gif);base64,)[A-Za-z0-9+/]+={0,2}$", 
            message = "Profile picture must be a valid base64 encoded image")
    private String profilePicture;
}