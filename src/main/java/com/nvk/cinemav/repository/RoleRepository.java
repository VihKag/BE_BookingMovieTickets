package com.nvk.cinemav.repository;

import com.nvk.cinemav.entity.Role;
import com.nvk.cinemav.type.UserRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findRolesByRoleName(UserRole role);
}
