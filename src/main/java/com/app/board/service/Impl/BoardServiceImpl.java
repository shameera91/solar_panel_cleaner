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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by Shameera on May, 2019
 */

@Service
public class BoardServiceImpl implements BoardService {

    private static final Double WATER_PER_SPRINKLER = 1.5;
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

        setBoardUsers(board, request);

        Board savedBoard = boardRepository.save(board);

        List<AutoWashDaysDTO> autoWashDays = request.getAutoWashDays();
        setBoardWashDays(savedBoard, autoWashDays);
        informServerOfWashDays(savedBoard, autoWashDays);

    }

    private void informServerOfWashDays(Board savedBoard, List<AutoWashDaysDTO> autoWashDays) {
        String s = savedBoard.getSimNumber();
        String d = getDaysString(autoWashDays);
        String h = savedBoard.getWashTime();
        String tz = "";
        try {
            URL url = new URL("http://localhost:8000/set?s=" + s + "&d=" + d + "&h=" + h + "&tz=" + tz + "&o=s");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getDaysString(List<AutoWashDaysDTO> autoWashDays) {
        char[] days = {'0', '0', '0', '0', '0', '0', '0'};
        for (AutoWashDaysDTO autoWashDaysDTO : autoWashDays) {
            String day = autoWashDaysDTO.getDay();
            switch (day) {
                case "sun":
                    days[0] = autoWashDaysDTO.isSelected() ? '1' : '0';
                    break;
                case "mon":
                    days[1] = autoWashDaysDTO.isSelected() ? '1' : '0';
                    break;
                case "tue":
                    days[2] = autoWashDaysDTO.isSelected() ? '1' : '0';
                    break;
                case "wed":
                    days[3] = autoWashDaysDTO.isSelected() ? '1' : '0';
                    break;
                case "thu":
                    days[4] = autoWashDaysDTO.isSelected() ? '1' : '0';
                    break;
                case "fri":
                    days[5] = autoWashDaysDTO.isSelected() ? '1' : '0';
                    break;
                case "sat":
                    days[6] = autoWashDaysDTO.isSelected() ? '1' : '0';
                    break;
            }
        }
        return String.valueOf(days);
    }


    /* public static void main(String[] args) {
         Calendar c = Calendar.getInstance();
         c.add(Calendar.DAY_OF_WEEK,4);
         int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
         System.out.println(dayOfWeek + c.getDisplayName());
     }
 */
    @Override
    public List<AllBoardDTO> getBoards(Integer userId) {
        Optional<User> userById = userRepository.findById(userId);
        if (userById.isPresent()) {

            List<Board> allBoards = boardRepository.findAll();
            List<Board> allBoarForUser = new ArrayList<>();

            if (userById.get().isAdmin()) {

                return prepareBoardDetails(allBoards);

            } else {

                for (Board b : allBoards) {
                    for (User u : b.getUsers()) {
                        if (u.getId().equals(userId)) {
                            allBoarForUser.add(b);
                        }
                    }
                }

                return prepareBoardDetails(allBoarForUser);

            }
        } else {
            return null;
        }
    }

    @Override
    public void editBoard(Board board, AddBoardRequest request) {
        board.setBoardIdentity(request.getBoardIdentity());
        board.setSimNumber(request.getSimNumber());
        board.setContactName(request.getContactName());
        board.setLocation(request.getLocation());

        setBoardUsers(board, request);
        board.setWashTime(request.getWashTime());
        board.setWaterPerWash(request.getWaterPerWash());
        boardRepository.save(board);

        List<AutoWashDaysDTO> autoWash = request.getAutoWashDays();

        boardWashDaysRepository.deleteBoardWashDaysByBoardId(board.getId()); /* deleting current wash days*/


        setBoardWashDays(board, autoWash);
        informServerOfWashDays(board, autoWash);
    }

    private void setBoardUsers(Board board, AddBoardRequest request) {
        Integer[] userIds = request.getUsers();
        Set<User> boardUsers = new HashSet<>();
        for (int i = 0; i < userIds.length; i++) {
            boardUsers.add(userRepository.findById(userIds[i]).get());
        }
        board.setUsers(boardUsers);
    }

    private void setBoardWashDays(Board board, List<AutoWashDaysDTO> autoWash) {
        for (AutoWashDaysDTO days : autoWash) {
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
    public void deleteBoard(Board board, Integer id) {

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
        for (BoardWashDays bwdays : boardWashDaysList) {
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
        List<ViewMessageDTO> messageList = null;
        try {
            String sim = getBoardById(boardId).get().getSimNumber();
            messageList = getMessagesFromServer(sim);
        } catch (IOException e) {
            e.printStackTrace();// TODO : handle
        }

        return messageList;
    }

    @Override
    public void addSms(String sim, String text) {
        try {
            URL url = new URL("http://localhost:8000/sendSms?sim=" + sim + "&msg=" + text);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* public static void main(String[] args) throws IOException {
        BoardServiceImpl boardService = new BoardServiceImpl();
        //((BoardServiceImpl) boardService).addSms("+0000000000", "banana");
        boardService.;
        System.out.println("hello");
    }*/


    private List<ViewMessageDTO> getMessagesFromServer(String sim) throws IOException {
        URL url = new URL("http://localhost:8000/getMessages?sim=" + sim);
        String readLine = null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        List<String> response = new ArrayList<>();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            while ((readLine = in.readLine()) != null) {
                response.add(readLine);
            }
            in.close();
            // print result
            //GetAndPost.POSTRequest(response.toString());
        } else {
            System.out.println("can't get messages from server");
        }
        return getMessageDtoFromInputString(response);
    }

    private List<ViewMessageDTO> getMessageDtoFromInputString(List<String> inputLine) {
        List<ViewMessageDTO> dtos = new ArrayList<>();
        for (String s : inputLine) {
            ViewMessageDTO viewMessageDTO = new ViewMessageDTO();
            viewMessageDTO.setDateTime(s.split("@~")[0]);
            viewMessageDTO.setMesage(s.split("@~")[1]);
            dtos.add(viewMessageDTO);
        }
        return dtos;
    }


    public List<AllBoardDTO> prepareBoardDetails(List<Board> allBoards) {
        List<AllBoardDTO> preparedBoardList = new ArrayList<>();
        Map<String, WashDto> lastWashes = getLastWashForAllBoards();
        for (Board b : allBoards) {
            AllBoardDTO allBoardDTO = new AllBoardDTO();
            allBoardDTO.setId(b.getId());
            allBoardDTO.setBoardIdentity(b.getBoardIdentity());
            allBoardDTO.setLocation(b.getLocation());
            allBoardDTO.setLastWash(lastWashes.get(b.getSimNumber()) == null ? null : lastWashes.get(b.getSimNumber()).getWashTime());
            allBoardDTO.setStatus(allBoardDTO.getLastWash() == null ? "ERROR" : "OK");
            allBoardDTO.setNumberOfWashes(lastWashes.get(b.getSimNumber()) == null ? 0 : lastWashes.get(b.getSimNumber()).getNumberOfWashes());
            allBoardDTO.setPhone(b.getSimNumber());
            allBoardDTO.setBoardName(b.getContactName());
            List<BoardWashDays> boardWashDaysList = boardWashDaysRepository.findByBoardId(b.getId());

            String dys = "";
            int count = 1;
            for (BoardWashDays washDays : boardWashDaysList) {

                dys += washDays.getDay();
                if (count < boardWashDaysList.size()) {
                    dys += ",";
                    count++;
                }
            }
            allBoardDTO.setWashDateTime(b.getWashTime() + " " + dys);
            allBoardDTO.setWaterPerWash(b.getWaterPerWash());
            allBoardDTO.setFactor(WATER_PER_SPRINKLER);
            preparedBoardList.add(allBoardDTO);
        }
        return preparedBoardList;
    }

    private Map<String, WashDto> getLastWashForAllBoards() {

        Map<String, WashDto> response = new HashMap<>();
        try {
            URL url = new URL("http://localhost:8000/getLastWashes");
            String readLine = null;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                while ((readLine = in.readLine()) != null) {
                    WashDto dto = new WashDto();
                    String sim = readLine.split("@~")[0];
                    String time = readLine.split("@~")[1];
                    String totalWater = readLine.split("@~")[2];
                    dto.setWashTime(time);
                    dto.setNumberOfWashes(Integer.parseInt(totalWater));
                    response.put(sim, dto);
                }
                in.close();
                // print result
                //GetAndPost.POSTRequest(response.toString());
            } else {
                System.out.println("GET NOT WORKED");
            }
        } catch (Exception e) {
            System.out.println("Cant connect to remoteOperate system");
            ;
        }
        return response;
    }

}
