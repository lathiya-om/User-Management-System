package com.exhibyt.UserManagment.Services;


import com.exhibyt.UserManagment.Constant.RoleName;

public interface AdminService {

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