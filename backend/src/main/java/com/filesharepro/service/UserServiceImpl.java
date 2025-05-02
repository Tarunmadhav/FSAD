package com.filesharepro.service;

import com.filesharepro.dto.UserProfileDto;
import com.filesharepro.entity.User;
import com.filesharepro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public User updateUserProfile(Long userId, UserProfileDto profileDto) {
        User user = getUserById(userId);

        if (profileDto.getProfileName() != null) {
            user.setProfileName(profileDto.getProfileName());
        }
        
        if (profileDto.getProfilePicture() != null) {
            user.setProfilePicture(profileDto.getProfilePicture());
        }

        return userRepository.save(user);
    }
}