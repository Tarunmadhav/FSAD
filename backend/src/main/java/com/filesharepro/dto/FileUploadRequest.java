package com.filesharepro.dto;

import com.filesharepro.validation.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadRequest {
    @NotNull(message = "File is required", groups = ValidationGroups.Create.class)
    private MultipartFile file;

    @Size(max = 1000, message = "Description must not exceed 1000 characters", 
          groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String description;

    private Boolean isPublic = false;
}