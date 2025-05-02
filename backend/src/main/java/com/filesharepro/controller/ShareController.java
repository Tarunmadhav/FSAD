package com.filesharepro.controller;

import com.filesharepro.dto.CreateShareKeyRequest;
import com.filesharepro.dto.ShareKeyDto;
import com.filesharepro.dto.FileInfoDto;
import com.filesharepro.entity.User;
import com.filesharepro.security.UserDetailsImpl;
import com.filesharepro.service.SharingService;
import com.filesharepro.service.FileStorageService;
import com.filesharepro.validation.SharingKey;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

import java.util.List;

@RestController
@RequestMapping("/api/share")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Share", description = "File sharing management APIs")
@SecurityRequirement(name = "bearerAuth")
public class ShareController {

    @Autowired
    private SharingService sharingService;

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(summary = "Generate sharing key", description = "Create a new sharing key for a file with optional expiry date and usage limit")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sharing key created successfully",
            content = @Content(schema = @Schema(implementation = ShareKeyDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "403", description = "Not authorized to share this file"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @PostMapping("/key")
    public ResponseEntity<ShareKeyDto> generateSharingKey(
            @Valid @RequestBody CreateShareKeyRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User owner = new User();
        owner.setId(userDetails.getId());
        ShareKeyDto shareKey = sharingService.createShareKey(
            request.getFileId(), 
            owner, 
            request.getExpiresAt(), 
            request.getMaxUses()
        );
        return ResponseEntity.ok(shareKey);
    }

    @Operation(summary = "Validate sharing key", description = "Validate a sharing key and get basic file information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sharing key is valid",
            content = @Content(schema = @Schema(implementation = FileInfoDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid or expired sharing key"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @GetMapping("/validate/{key}")
    public ResponseEntity<FileInfoDto> validateSharingKey(
            @SharingKey @Parameter(description = "The sharing key to validate") @PathVariable String key) {
        ShareKeyDto shareKey = sharingService.getShareKey(key);
        FileInfoDto fileInfo = fileStorageService.getFileInfo(shareKey.getFileUrl().substring("/api/files/".length()));
        FileInfoDto minimalInfo = new FileInfoDto();
        minimalInfo.setFilename(fileInfo.getFilename());
        minimalInfo.setSize(fileInfo.getSize());
        minimalInfo.setContentType(fileInfo.getContentType());
        return ResponseEntity.ok(minimalInfo);
    }

    @Operation(summary = "Download shared file", description = "Download a file using a sharing key")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "File downloaded successfully",
            content = @Content(mediaType = "application/octet-stream")),
        @ApiResponse(responseCode = "400", description = "Invalid or expired sharing key"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @GetMapping("/download/{key}")
    public ResponseEntity<Resource> downloadSharedFile(
            @SharingKey @Parameter(description = "The sharing key to download the file") @PathVariable String key) {
        ShareKeyDto shareKey = sharingService.getShareKey(key);
        String fileKey = shareKey.getFileUrl().substring("/api/files/".length());
        FileInfoDto fileInfo = fileStorageService.getFileInfo(fileKey);
        Resource resource = fileStorageService.loadFileAsResource(fileKey);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileInfo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + fileInfo.getOriginalFilename() + "\"")
                .body(resource);
    }

    @Operation(summary = "Get file sharing keys", description = "Get all active sharing keys for a specific file")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of sharing keys retrieved successfully",
            content = @Content(schema = @Schema(implementation = ShareKeyDto.class))),
        @ApiResponse(responseCode = "403", description = "Not authorized to view sharing keys"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @GetMapping("/keys/{fileId}")
    public ResponseEntity<List<ShareKeyDto>> getFileShareKeys(
            @Parameter(description = "ID of the file to get sharing keys for") @PathVariable Long fileId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User owner = new User();
        owner.setId(userDetails.getId());
        List<ShareKeyDto> shareKeys = sharingService.getFileShareKeys(fileId, owner);
        return ResponseEntity.ok(shareKeys);
    }

    @Operation(summary = "Revoke sharing key", description = "Revoke an active sharing key")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sharing key revoked successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired sharing key"),
        @ApiResponse(responseCode = "403", description = "Not authorized to revoke this sharing key")
    })
    @DeleteMapping("/keys/{key}")
    public ResponseEntity<?> revokeShareKey(
            @SharingKey @Parameter(description = "The sharing key to revoke") @PathVariable String key,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User owner = new User();
        owner.setId(userDetails.getId());
        sharingService.revokeShareKey(key, owner);
        return ResponseEntity.ok().build();
    }
}