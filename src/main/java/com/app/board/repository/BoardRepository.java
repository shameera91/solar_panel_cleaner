package com.app.board.repository;

import com.app.board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Shameera on May, 2019
 */
public interface BoardRepository extends JpaRepository<Board,Integer> {

	Optional<Board> findById(Integer id);

	Optional<Board> findByBoardIdentity(Integer boardIdentity);

}
