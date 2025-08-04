package com.exhibyt.UserManagment.Repository;

import com.exhibyt.UserManagment.Constant.RoleName;
import com.exhibyt.UserManagment.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}
