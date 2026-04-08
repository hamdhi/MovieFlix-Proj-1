package com.myjwtai.jwtai.repository;

import com.myjwtai.jwtai.entity.Role;
import com.myjwtai.jwtai.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
