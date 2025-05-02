package com.filesharepro.service;

import com.filesharepro.dto.UserProfileDto;
import com.filesharepro.entity.User;

public interface UserService {
    User getUserById(Long id);
    User updateUserProfile(Long userId, UserProfileDto profileDto);
}