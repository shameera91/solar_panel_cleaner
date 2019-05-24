package com.app.board.service.Impl;

import com.app.board.model.Board;
import com.app.board.model.BoardWashDays;
import com.app.board.model.User;
import com.app.board.payload.*;
import com.app.board.repository.BoardRepository;
import com.app.board.repository.BoardWashDaysRepository;
import com.app.board.repository.UserRepository;
import com.app.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Shameera on May, 2019
 */

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BoardRepository boardRepository;

	@Autowired
	BoardWashDaysRepository boardWashDaysRepository;

	@Override
	public Optional<Board> getBoardById(Integer id) {
		return boardRepository.findById(id);
	}

	@Override
	public Optional<Board> getBoardByIdentification(Integer boardIdentity) {
		return boardRepository.findByBoardIdentity(boardIdentity);
	}

	@Override
	public void saveBoard(AddBoardRequest request) {
		Board board = new Board();

		board.setBoardIdentity(request.getBoardIdentity());
		board.setContactName(request.getContactName());
		board.setLocation(request.getLocation());
		board.setSimNumber(request.getSimNumber());
		board.setWashTime(request.getWashTime());
		board.setWaterPerWash(request.getWaterPerWash());

		Integer[] users = request.getUsers();
		Set<User> userList = new HashSet<>();
		for(int i=0;i<users.length;i++){
			userList.add(userRepository.findById(users[i]).get());
		}

		board.setUsers(userList);

		Board savedBoard = boardRepository.save(board);

		List<AutoWashDaysDTO> autoWashDays = request.getAutoWashDays();
		for(AutoWashDaysDTO days:autoWashDays){
			BoardWashDays boardWashDays = new BoardWashDays();
			//if(days.isSelected()){
				boardWashDays.setBoard(savedBoard);
				boardWashDays.setDay(days.getDay());
				boardWashDays.setSelected(days.isSelected());
				boardWashDaysRepository.save(boardWashDays);
			//}

		}

	}

	@Override
	public List<AllBoardDTO> getBoards(Integer userId) {
		Optional<User> userById = userRepository.findById(userId);
		if(userById.isPresent()){

			List<Board> allBoards  = boardRepository.findAll();
			List<Board> allBoarForUser = new ArrayList<>();

			if(userById.get().isAdmin()){

				return prepareBoardDetails(allBoards);

			 }else{

				for(Board b : allBoards){
					for (User u: b.getUsers()){
						if(u.getId() == userId){
							allBoarForUser.add(b);
						}
					}
				}

				return prepareBoardDetails(allBoarForUser);

			}
		}else{
			return null;
		}
	}

	@Override
	public void editBoard(Board board,AddBoardRequest request) {
		board.setBoardIdentity(request.getBoardIdentity());
		board.setSimNumber(request.getSimNumber());
		board.setContactName(request.getContactName());
		board.setLocation(request.getLocation());

		Integer[] userIds = request.getUsers();
		Set<User> boardUsers = new HashSet<>();
		for (int i=0;i<userIds.length;i++){
			boardUsers.add(userRepository.findById(userIds[i]).get());
		}
		board.setUsers(boardUsers);
		board.setWashTime(request.getWashTime());
		board.setWaterPerWash(request.getWaterPerWash());
		boardRepository.save(board);

		List<AutoWashDaysDTO> autoWash = request.getAutoWashDays();

		boardWashDaysRepository.deleteBoardWashDaysByBoardId(board.getId()); /* deleting current wash days*/


		for(AutoWashDaysDTO days:autoWash){
			BoardWashDays boardWashDays = new BoardWashDays();
			//if(days.isSelected()){
				boardWashDays.setBoard(board);
				boardWashDays.setDay(days.getDay());
				boardWashDays.setSelected(days.isSelected());
				boardWashDaysRepository.save(boardWashDays);
			//}
		}

	}

	@Override
	public void deleteBoard(Board board,Integer id) {

		boardRepository.delete(id);
		boardWashDaysRepository.deleteBoardWashDaysByBoardId(board.getId());

	}

	@Override
	public BoardDetailDTO getBoardsByBoardId(Integer boardId) {
		Board boardById = boardRepository.findById(boardId).get();

		BoardDetailDTO detailDTO = new BoardDetailDTO();
		detailDTO.setBoardIdentity(boardById.getBoardIdentity());
		detailDTO.setContactName(boardById.getContactName());
		detailDTO.setLocation(boardById.getLocation());
		detailDTO.setSimNumber(boardById.getSimNumber());
		detailDTO.setWashTime(boardById.getWashTime());
		detailDTO.setWaterPerWash(boardById.getWaterPerWash());
		detailDTO.setUsers(boardById.getUsers());
		//AddBoardRequest boardDetails = new AddBoardRequest();

		/*boardDetails.setBoardIdentity(boardById.getBoardIdentity());
		boardDetails.setContactName(boardById.getContactName());
		boardDetails.setLocation(boardById.getLocation());
		boardDetails.setSimNumber(boardById.getSimNumber());
		boardDetails.setWashTime(boardById.getWashTime());
		boardDetails.setWaterPerWash(boardById.getWaterPerWash());*/

		//Set<User> users = boardById.getUsers();

		/*Integer []boardIds= new Integer[users.size()]; int i=0;
		if(!users.isEmpty()){
			for (User u : users){
				boardIds[i] = u.getId();
				i++;
			}
		}*/

		//boardDetails.setUsers(boardIds);

		List<BoardWashDays> boardWashDaysList = boardWashDaysRepository.findByBoardId(boardById.getId());

		List<AutoWashDaysDTO> autoList = new ArrayList<>();
		for(BoardWashDays bwdays:boardWashDaysList){
			AutoWashDaysDTO autoWashDaysDTO = new AutoWashDaysDTO();
			autoWashDaysDTO.setDay(bwdays.getDay());
			autoWashDaysDTO.setSelected(bwdays.getIsSelected());
			autoList.add(autoWashDaysDTO);
		}

		detailDTO.setAutoWashDays(autoList);
		return detailDTO;
	}

	@Override
	public List<ViewMessageDTO> getMessagesBySimNumber(Integer boardId) {
		/* query the sim number and get details from other db using that sim number*/
		List<ViewMessageDTO> messageList = new ArrayList<>();
		ViewMessageDTO message1 = new ViewMessageDTO("Start","2019-05-21 10:23");
		ViewMessageDTO message2 = new ViewMessageDTO("Internal Error","2019-05-21 10:23");
		ViewMessageDTO message3 = new ViewMessageDTO("Success","2019-05-21 10:23");
		ViewMessageDTO message4 = new ViewMessageDTO("Start","2019-05-21 11:25");
		messageList.add(message1);
		messageList.add(message2);
		messageList.add(message3);
		messageList.add(message4);
		return messageList;
	}

	public List<AllBoardDTO> prepareBoardDetails(List<Board> allBoards){
		List<AllBoardDTO> preparedBoardList = new ArrayList<>();
		for(Board b:allBoards){
			AllBoardDTO allBoardDTO = new AllBoardDTO();
			allBoardDTO.setId(b.getId());
			allBoardDTO.setBoardIdentity(b.getBoardIdentity());
			allBoardDTO.setLocation(b.getLocation());
			allBoardDTO.setStatus("Ok");  /*hard coded values*/
			allBoardDTO.setLastWash("2019-May-21 14:00"); /*hard coded values*/

			List<BoardWashDays> boardWashDaysList = boardWashDaysRepository.findByBoardId(b.getId());

			String dys = ""; int count = 1;
			for(BoardWashDays washDays:boardWashDaysList){

				dys += washDays.getDay();
				if(count < boardWashDaysList.size()){
					dys += ",";
					count++;
				}
			}
			allBoardDTO.setWashDateTime(b.getWashTime()+" "+dys);
			preparedBoardList.add(allBoardDTO);
		}
		return preparedBoardList;
	}


}
