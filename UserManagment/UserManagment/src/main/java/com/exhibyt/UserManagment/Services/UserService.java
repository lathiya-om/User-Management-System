package com.exhibyt.UserManagment.Services;

import com.exhibyt.UserManagment.Dto.UpdateProfileRequest;
import com.exhibyt.UserManagment.Dto.UpdateUserRequest;
import com.exhibyt.UserManagment.Dto.UserProfileDto;

public interface UserService {

    /**
     * Gets the profile of the currently authenticated user.
     *
     * @return UserProfileDto containing user profile details.
     */
    UserProfileDto getCurrentUserProfile();

    /**
     * Updates the profile of the currently authenticated user.
     *
     * @param request The updated profile data.
     */
    void updateCurrentUser(UpdateUserRequest request);

    /**
     * Soft deletes the currently authenticated user (marks as inactive instead of deleting).
     */
    void softDeleteCurrentUser();

    /**
     * Disables the currently authenticated user's account.
     * Useful for temporary deactivation.
     */
    void disableCurrentUser();

    /**
     * Enables the currently authenticated user's account.
     * Reverses the effect of account disablement.
     */
    void enableCurrentUser();

    /**
     * Updates the profile of a specific user by ID (admin functionality).
     *
     * @param userId The ID of the user to update.
     * @param request The updated profile information (username, email, password).
     */
    void updateProfile(Long userId, UpdateProfileRequest request);
}
