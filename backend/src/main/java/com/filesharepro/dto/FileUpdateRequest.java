package com.filesharepro.dto;

import com.filesharepro.validation.ValidationGroups;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileUpdateRequest {
    @Size(max = 255, message = "Filename must not exceed 255 characters", 
          groups = ValidationGroups.Update.class)
    @Pattern(regexp = "^[\\w\\-. ]+$", 
            message = "Filename can only contain letters, numbers, dots, dashes, underscores and spaces",
            groups = ValidationGroups.Update.class)
    private String filename;

    private Boolean isPublic;

    @Future(message = "Expiry date must be in the future", 
           groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private LocalDateTime expiryDate;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters",
          groups = ValidationGroups.Update.class)
    private String description;
}