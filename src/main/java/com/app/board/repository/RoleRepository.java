package com.app.board.repository;

import com.app.board.model.Role;
import com.app.board.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Shameera on May, 2019
 */
public interface RoleRepository extends JpaRepository<Role,Integer> {

	Optional<Role> findByName(RoleName roleName);
}
