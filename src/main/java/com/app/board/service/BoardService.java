package com.app.board.service;

import com.app.board.model.Board;
import com.app.board.payload.AddBoardRequest;
import com.app.board.payload.AllBoardDTO;
import com.app.board.payload.BoardDetailDTO;
import com.app.board.payload.ViewMessageDTO;

import java.util.List;
import java.util.Optional;

/**
 * Created by Shameera on May, 2019
 */
public interface BoardService {

	Optional<Board> getBoardById(Integer id);

	Optional<Board> getBoardByIdentification(Integer identification);

	void saveBoard(AddBoardRequest request);

	List<AllBoardDTO> getBoards(Integer userId);

	void editBoard(Board board,AddBoardRequest request);

	void deleteBoard(Board board,Integer id);

	BoardDetailDTO getBoardsByBoardId(Integer boardId);

	List<ViewMessageDTO> getMessagesBySimNumber(Integer boardId);
}
