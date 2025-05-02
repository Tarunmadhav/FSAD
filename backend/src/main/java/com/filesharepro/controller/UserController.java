package com.filesharepro.controller;

import com.filesharepro.dto.UserProfileDto;
import com.filesharepro.entity.User;
import com.filesharepro.security.UserDetailsImpl;
import com.filesharepro.service.UserService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Users", description = "User profile management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get current user profile", description = "Get the profile information of the currently authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserProfileDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.getUserById(userDetails.getId());
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setProfileName(user.getProfileName());
        profileDto.setProfilePicture(user.getProfilePicture());
        return ResponseEntity.ok(profileDto);
    }

    @Operation(summary = "Update user profile", description = "Update the profile information of the currently authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile updated successfully",
            content = @Content(schema = @Schema(implementation = UserProfileDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/me/profile")
    public ResponseEntity<UserProfileDto> updateUserProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UserProfileDto profileDto) {
        User updatedUser = userService.updateUserProfile(userDetails.getId(), profileDto);
        
        UserProfileDto responseDto = new UserProfileDto();
        responseDto.setProfileName(updatedUser.getProfileName());
        responseDto.setProfilePicture(updatedUser.getProfilePicture());
        return ResponseEntity.ok(responseDto);
    }
}