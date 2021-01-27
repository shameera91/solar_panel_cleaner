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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        board.setLon(request.getLon());
        board.setLat(request.getLat());

        setBoardUsers(board, request);

        Board savedBoard = boardRepository.save(board);

        List<AutoWashDaysDTO> autoWashDays = request.getAutoWashDays();
        setBoardWashDays(savedBoard, autoWashDays);
        informServerOfWashDays(savedBoard, autoWashDays);

    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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
    public List<BoardDTO> getBoards(Integer userId) {
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
        board.setLon(request.getLon());
        board.setLat(request.getLat());
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
        detailDTO.setLat(boardById.getLat());
        detailDTO.setLon(boardById.getLon());
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
        return getMessageDtoFromInputString(response, sim);
    }

    private List<ViewMessageDTO> getMessageDtoFromInputString(List<String> inputLine, String sim) {
        List<ViewMessageDTO> dtos = new ArrayList<>();
        for (String s : inputLine) {
            ViewMessageDTO viewMessageDTO = new ViewMessageDTO();
            viewMessageDTO.setDateTime(s.split("@~")[0]);
            if (viewMessageDTO.getDateTime() != null) {
                viewMessageDTO.setDateTime(fixTimeZone(viewMessageDTO.getDateTime(), sim));
            }
            viewMessageDTO.setMesage(s.split("@~")[1]);
            dtos.add(viewMessageDTO);
        }
        return dtos;
    }


    public List<BoardDTO> prepareBoardDetails(List<Board> allBoards) {
        List<BoardDTO> preparedBoardList = new ArrayList<>();
        Map<String, WashDto> lastWashes = getLastWashForAllBoards();
        for (Board b : allBoards) {
            BoardDTO boardDTO = new BoardDTO();
            boardDTO.setId(b.getId());
            boardDTO.setBoardIdentity(b.getBoardIdentity());
            boardDTO.setLocation(b.getLocation());
            boardDTO.setLastWash(lastWashes.get(b.getSimNumber()) == null ? null : lastWashes.get(b.getSimNumber()).getWashTime());
            boardDTO.setLastWash(fixTimeZone(boardDTO.getLastWash(), b.getSimNumber()));
            boardDTO.setStatus(getBoardStatus(b, boardDTO.getLastWash()));
            boardDTO.setNumberOfWashes(lastWashes.get(b.getSimNumber()) == null ? 0 : lastWashes.get(b.getSimNumber()).getNumberOfWashes());
            boardDTO.setPhone(b.getSimNumber());
            boardDTO.setBoardName(b.getContactName());
            boardDTO.setLat(b.getLat());
            boardDTO.setLon(b.getLon());
            List<BoardWashDays> boardWashDaysList = boardWashDaysRepository.findByBoardId(b.getId());

            String days = "";
            int count = 1;
            for (BoardWashDays washDays : boardWashDaysList) {

                days += washDays.getDay();
                if (count < boardWashDaysList.size()) {
                    days += ",";
                    count++;
                }
            }
            boardDTO.setWashDateTime(b.getWashTime() + " " + days);
            boardDTO.setWaterPerWash(b.getWaterPerWash());
            boardDTO.setFactor(WATER_PER_SPRINKLER);
            preparedBoardList.add(boardDTO);
        }
        return preparedBoardList;
    }

    private String fixTimeZone(String lastWash, String phone) {
        if (1 == 1) return lastWash;
        try {
            Calendar calendar = getLastWashCal(lastWash);
            calendar.add(Calendar.HOUR_OF_DAY, -4);
            return sdf.format(calendar.getTime());

        } catch (Exception e) {
            return lastWash;
        }
    }

    private String getBoardStatus(Board b, String lastWash) {
        if (null == lastWash || lastWash.isEmpty()) return "ERROR";
        // if (!(null == lastWash || lastWash.isEmpty())) return "OK";
        List<BoardWashDays> boardWashDaysList = boardWashDaysRepository.findByBoardId(b.getId());
        if (noWashesDefined(boardWashDaysList)) return "OK";

        Calendar now = Calendar.getInstance();
        //USA now.add(Calendar.HOUR,-10);

        Calendar lastWashCal = getLastWashCal(lastWash);
        Calendar aWeekAgo = Calendar.getInstance();
        aWeekAgo.add(Calendar.WEEK_OF_MONTH, -1);
        //USA  aWeekAgo.add(Calendar.HOUR,-10);

        if (lastWashCal.before(aWeekAgo))
            return "ERROR";  // washes are scheduled, but last one was more than a week ago

        int hourNow = now.get(Calendar.HOUR_OF_DAY);
        int minNow = now.get(Calendar.MINUTE);
        int washHour = Integer.parseInt(b.getWashTime().substring(0, 2));
        int washMin = Integer.parseInt(b.getWashTime().substring(3, 5));
        String daysScheduledToWash = getWashDays(boardWashDaysList);
        if (washHour > hourNow || (washHour == hourNow && washMin > minNow)) {
            now.add(Calendar.DAY_OF_MONTH, -1);
        }
        now.set(Calendar.HOUR_OF_DAY, washHour);
        now.set(Calendar.MINUTE, washMin);
        // now.add(Calendar.MINUTE, -241); //checking 241 mins before, in case of clock differances
        for (int i = 0; i < 7; i++) {
            String dayNow = now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US).toLowerCase();
            if (itWasSupposedToWork(dayNow, daysScheduledToWash)) {
                lastWashCal.add(Calendar.HOUR_OF_DAY, 1); //reducing 1 hour to prevent the 2 minutes bug
                if (now.after(lastWashCal)) return "ERROR"; //supposed to work but didnt
                else return "OK";
            }
            now.add(Calendar.DAY_OF_MONTH, -1);
        }

        return "OK";
    }

    private boolean itWasSupposedToWork(String dayNow, String daysScheduledToWash) {
        return daysScheduledToWash.contains(dayNow);
    }

    private String getWashDays(List<BoardWashDays> boardWashDaysList) {
        String daysScheduledToWash = "";

        for (BoardWashDays day : boardWashDaysList) {
            if (day.getIsSelected()) {
                daysScheduledToWash += day.getDay();
            }
        }
        return daysScheduledToWash;
    }

    private boolean noWashesDefined(List<BoardWashDays> boardWashDaysList) {
        if (boardWashDaysList.size() == 0) return true; // no days defined?
        boolean noDays = true;
        for (BoardWashDays day : boardWashDaysList) {
            if (day.getIsSelected()) noDays = false;
        }
        // no days selected
        return noDays;
    }

    private Calendar getLastWashCal(String lastWash) {
        Date date = null;
        try {
            date = sdf.parse(lastWash);
        } catch (ParseException e) {
            // Logger.getLogger(BoardServiceImpl.class)
        }
        Calendar lastWashCal = Calendar.getInstance();
        //USA-        Calendar lastWashCal = Calendar.getInstance(TimeZone.getTimeZone("GMT-7"));
        lastWashCal.setTime(date); //USA - REMARK OUT
        return lastWashCal;
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

    public static void main(String[] args) {
        int washMin = Integer.parseInt("10:31".substring(3, 5));
        System.out.println(washMin);
    }
}
