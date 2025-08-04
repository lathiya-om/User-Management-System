package com.exhibyt.UserManagment.Services;


import com.exhibyt.UserManagment.Dto.UpdateProfileRequest;

public interface UserService {

    /**
     * Updates the profile of a user.
     * @param userId The ID of the user to update.
     * @param request The updated profile information (username, email, password).
     */
    void updateProfile(Long userId, UpdateProfileRequest request);
}