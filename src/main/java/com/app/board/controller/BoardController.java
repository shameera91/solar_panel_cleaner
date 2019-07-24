package com.app.board.controller;

import com.app.board.model.Board;
import com.app.board.payload.*;
import com.app.board.service.BoardService;
import com.app.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static com.app.board.controller.Util.getCurrentUser;

/**
 * Created by Shameera on May, 2019
 */

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private static final Logger LOGGER = Logger.getLogger(BoardController.class.getName());
    private static final String WASH_NOW_TEXT = "p.1234relay.3s";
    private static final String COMM_CHECK_TEXT = "P.1234RECEP";

	@Autowired
	UserService userService;

	@Autowired
	BoardService boardService;

	@PostMapping("/requestCommCheck/{boardId}")
    public ResponseEntity<?> requestCommCheck(@PathVariable Integer boardId){
        LOGGER.warning("request comm ack for board: " + boardId + " - from user : " + getCurrentUser());
        return getResponseEntity(boardId, COMM_CHECK_TEXT);
    }

    private ResponseEntity<?> getResponseEntity(@PathVariable Integer boardId, String commCheckText) {
        ApiResponse response = new ApiResponse();
        Optional<Board> boardByIdentification = boardService.getBoardById(boardId);

        if(boardByIdentification.isPresent()){
            boardService.addSms(boardByIdentification.get().getSimNumber(), commCheckText);

            response.setMessage("Success");
            response.setError(false);
        }else{
            response.setMessage("No such a board available");
            response.setError(true);
        }
        return ResponseEntity.ok(response);
    }


	@PostMapping("/sendMessage/{boardId}")
	public ResponseEntity<?> sendMessage(@PathVariable Integer boardId,@RequestBody SendMessageRequest request){
		ApiResponse response = new ApiResponse();
		Optional<Board> board = boardService.getBoardById(boardId);


		if(board.isPresent()){
            boardService.addSms(board.get().getSimNumber(), request.getMessage());
			response.setMessage("Success");
			response.setError(false);
		}else{
			response.setMessage("No such a board available");
			response.setError(true);
		}
        LOGGER.warning("request send Message to board: " + boardId + " - from user : " + getCurrentUser() + ", message text = " + request.getMessage());
        return ResponseEntity.ok(response);
	}

	@PostMapping("/washNow/{boardId}")
	public ResponseEntity<?> washNow(@PathVariable Integer boardId){
        LOGGER.warning("request wash now for board: " + boardId + " - from user : " + getCurrentUser());

        return getResponseEntity(boardId, WASH_NOW_TEXT);
    }


	@GetMapping("/viewMessages/{boardId}")
	public ResponseEntity<?> getMessagesForBoard(@PathVariable Integer boardId){
		ApiResponse response = new ApiResponse();
		Optional<Board> boardById = boardService.getBoardById(boardId);
		if(boardById.isPresent()){
			List<ViewMessageDTO> messagesBySimNumber = boardService.getMessagesBySimNumber(boardId);
			response.setMessage("Success");
			response.setError(false);
			response.setData(messagesBySimNumber);
		}else{
			response.setError(true);
			response.setMessage("No such a board available");
		}
        LOGGER.warning("request view messages for board: " + boardId + " - from user : " + getCurrentUser() + ", response was:" + response.getMessage());
        return ResponseEntity.ok(response);
	}

	@GetMapping("/getBoard/{boardId}")
	public ResponseEntity<?> getBoardsByBoardId(@PathVariable Integer boardId){
		ApiResponse response = new ApiResponse();
		Optional<Board> boardById = boardService.getBoardById(boardId);
		if(boardById.isPresent()){
			BoardDetailDTO boardDetails = boardService.getBoardsByBoardId(boardId);
			response.setError(false);
			response.setMessage("Success");
			response.setData(boardDetails);
		}else{
			response.setError(true);
			response.setMessage("Board id not found");
		}
        LOGGER.warning("request getBoard for board: " + boardId + " - from user : " + getCurrentUser() + ", response was:" + response.getMessage());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteBoard/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Integer boardId) {
		ApiResponse response = new ApiResponse();
        Optional<Board> boardById = boardService.getBoardById(boardId);
		if(boardById.isPresent()) {
            boardService.deleteBoard(boardById.get(), boardId);
			response.setError(false);
			response.setMessage("Success");
		}else{
			response.setError(true);
			response.setMessage("No such board available");
		}
        LOGGER.warning("request delete board: " + boardId + " - from user : " + getCurrentUser() + ", response was:" + response.getMessage());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/editBoard/{boardId}")
    public ResponseEntity<?> editBoard(@PathVariable Integer boardId, @RequestBody AddBoardRequest request) {
		ApiResponse response = new ApiResponse();
        Optional<Board> boardById = boardService.getBoardById(boardId);
		if(boardById.isPresent()){
			boardService.editBoard(boardById.get(),request);
			response.setError(false);
			response.setMessage("Success");
		}else{
			response.setError(true);
			response.setMessage("No such board available");
		}
        LOGGER.warning("request edit board: " + boardId + " - from user : " + getCurrentUser() + ", response was:" + response.getMessage());
        return ResponseEntity.ok(response);
	}

	@GetMapping("/getBoards/{userId}")
	public ResponseEntity<?> getBoards(@PathVariable Integer userId){
		ApiResponse response = new ApiResponse();
        List<BoardDTO> boards = boardService.getBoards(userId);
		response.setError(false);
		response.setMessage("Success");
		response.setData(boards);
        LOGGER.warning("request getBoards - from user : " + getCurrentUser() + ", response was:" + response.getMessage());
        return ResponseEntity.ok(response);
	}

	@PostMapping("/addBoard")
	public ResponseEntity<?> addBoard(@RequestBody AddBoardRequest request){
		ApiResponse response = new ApiResponse();
		try {
			boardService.saveBoard(request);
			response.setMessage("Success");
			response.setError(false);
		}catch (Exception e){
			response.setMessage("Board adding error");
			response.setError(true);
		}

        LOGGER.warning("request add board: " + request.getBoardIdentity() + " - from user : " + getCurrentUser() + ", response was:" + response.getMessage());
        return ResponseEntity.ok(response);
	}

}
