package com.app.board.repository;

import com.app.board.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Shameera on May, 2019
 */

public interface UserRepository extends JpaRepository<User,Integer> {

	Optional<User> findByEmail(String email);

	Optional<User> findById(Integer id);

	Boolean existsByEmail(String email);
}
