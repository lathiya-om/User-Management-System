package com.exhibyt.UserManagment.Services;

import com.exhibyt.UserManagment.Constant.RoleName;
import com.exhibyt.UserManagment.Dto.RegisterRequest;

public interface AdminService {

    /**
     * Creates a new ADMIN user.
     * @param request The registration request containing username, email, and password.
     */
    void createAdmin(RegisterRequest request);

    /**
     * Promotes an existing user to ADMIN.
     * @param username The username of the user to promote.
     */
    void promoteToAdmin(String username);

    /**
     * Performs a soft delete on a user account, marking it as deleted without removing the data.
     * @param userId The ID of the user to soft delete.
     */
    void softDeleteUser(Long userId);

    /**
     * Enables or disables a user account.
     * @param userId The ID of the user.
     * @param enable A boolean flag: true to enable, false to disable.
     */
    void enableOrDisableUser(Long userId, boolean enable);

    /**
     * Assigns a role to a user.
     * @param userId The ID of the user.
     * @param roleName The name of the role to assign.
     */
    void assignRole(Long userId, RoleName roleName);

    /**
     * Revokes a role from a user.
     * @param userId The ID of the user.
     * @param roleName The name of the role to revoke.
     */
    void revokeRole(Long userId, RoleName roleName);
}
