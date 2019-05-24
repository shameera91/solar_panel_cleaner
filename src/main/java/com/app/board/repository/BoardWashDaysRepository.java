package com.app.board.repository;

import com.app.board.model.BoardWashDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Shameera on May, 2019
 */
public interface BoardWashDaysRepository extends JpaRepository<BoardWashDays,Integer> {

	List<BoardWashDays> findByBoardId(Integer board);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM BoardWashDays b WHERE b.board.id = :id")
	void deleteBoardWashDaysByBoardId(@Param("id")Integer id);


}
