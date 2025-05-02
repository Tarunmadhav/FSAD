package com.filesharepro.controller;

import com.filesharepro.dto.FileInfoDto;
import com.filesharepro.dto.FileUploadRequest;
import com.filesharepro.dto.FileUpdateRequest;
import com.filesharepro.entity.User;
import com.filesharepro.security.UserDetailsImpl;
import com.filesharepro.service.FileStorageService;
import com.filesharepro.validation.FileKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Files", description = "File management APIs")
@SecurityRequirement(name = "bearerAuth")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(summary = "Upload file", description = "Upload a new file with optional metadata")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "File uploaded successfully",
            content = @Content(schema = @Schema(implementation = FileInfoDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid file or request parameters"),
        @ApiResponse(responseCode = "403", description = "Storage quota exceeded")
    })
    @PostMapping("/upload")
    public ResponseEntity<FileInfoDto> uploadFile(
            @Valid @ModelAttribute FileUploadRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User owner = new User();
        owner.setId(userDetails.getId());
        
        FileInfoDto fileInfo = fileStorageService.storeFile(request.getFile(), owner);
        
        if (request.getIsPublic() != null) {
            fileStorageService.updateFileVisibility(fileInfo.getFileKey(), owner, request.getIsPublic());
            fileInfo.setPublic(request.getIsPublic());
        }
        
        return ResponseEntity.ok(fileInfo);
    }

    @Operation(summary = "List user files", description = "Get a list of all files owned by the current user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Files retrieved successfully",
            content = @Content(schema = @Schema(implementation = FileInfoDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<FileInfoDto>> getUserFiles(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User owner = new User();
        owner.setId(userDetails.getId());
        List<FileInfoDto> files = fileStorageService.getAllUserFiles(owner);
        return ResponseEntity.ok(files);
    }

    @Operation(summary = "Download file", description = "Download a file by its key")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "File downloaded successfully",
            content = @Content(mediaType = "application/octet-stream")),
        @ApiResponse(responseCode = "403", description = "Not authorized to access this file"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @GetMapping("/{fileKey}")
    public ResponseEntity<Resource> downloadFile(
            @FileKey @Parameter(description = "Key of the file to download") @PathVariable String fileKey,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = new User();
        user.setId(userDetails.getId());

        if (!fileStorageService.verifyFileAccess(fileKey, user)) {
            return ResponseEntity.notFound().build();
        }

        FileInfoDto fileInfo = fileStorageService.getFileInfo(fileKey);
        Resource resource = fileStorageService.loadFileAsResource(fileKey);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileInfo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileInfo.getOriginalFilename() + "\"")
                .body(resource);
    }

    @Operation(summary = "Delete file", description = "Delete a file by its key")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "File deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Not authorized to delete this file"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @DeleteMapping("/{fileKey}")
    public ResponseEntity<?> deleteFile(
            @FileKey @Parameter(description = "Key of the file to delete") @PathVariable String fileKey,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User owner = new User();
        owner.setId(userDetails.getId());
        fileStorageService.deleteFile(fileKey, owner);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update file visibility", description = "Update the public/private status of a file")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "File visibility updated successfully"),
        @ApiResponse(responseCode = "403", description = "Not authorized to update this file"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @PutMapping("/{fileKey}/visibility")
    public ResponseEntity<?> updateFileVisibility(
            @FileKey @Parameter(description = "Key of the file to update") @PathVariable String fileKey,
            @Parameter(description = "New visibility status") @RequestParam boolean isPublic,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User owner = new User();
        owner.setId(userDetails.getId());
        fileStorageService.updateFileVisibility(fileKey, owner, isPublic);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update file metadata", description = "Update file metadata such as filename and expiry date")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "File metadata updated successfully",
            content = @Content(schema = @Schema(implementation = FileInfoDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Not authorized to update this file"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @PutMapping("/{fileKey}")
    public ResponseEntity<FileInfoDto> updateFile(
            @FileKey @Parameter(description = "Key of the file to update") @PathVariable String fileKey,
            @Valid @RequestBody FileUpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User owner = new User();
        owner.setId(userDetails.getId());
        
        FileInfoDto fileInfo = fileStorageService.updateFile(fileKey, owner, request);
        return ResponseEntity.ok(fileInfo);
    }
}