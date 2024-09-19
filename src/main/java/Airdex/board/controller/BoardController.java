package Airdex.board.controller;

import Airdex.board.entity.WorkoutTable;
import Airdex.board.entity.Workout;
import Airdex.board.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;

import Airdex.board.entity.DataType;
import java.util.stream.Collectors;

@Controller
public class BoardController {

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private Workout workout;

    @GetMapping("/board")
    public String mainPage(){
        return "main";
    }

    // 드롭다운을 통한 운동 추가완료
    @GetMapping("/board/write") //localhost:8080/board/write
    public String getDropdown(Model model) {

        List<List<String>> sortedLists = workoutService.sortByCategory(workout);
        List<String> back = sortedLists.get(0);
        List<String> chest = sortedLists.get(1);
        List<String> delt = sortedLists.get(2);
        List<String> leg = sortedLists.get(3);
        List<String> arm = sortedLists.get(4);

        model.addAttribute("backNames", back);
        model.addAttribute("chestNames", chest);
        model.addAttribute("deltNames", delt);
        model.addAttribute("legNames", leg);
        model.addAttribute("armNames", arm);

        return "boardwrite";
    }

    //데이터 저장 기능 구현(2024-04-08)
    @PostMapping("/board/write/savedata")
    @ResponseBody
    public String savedata(@RequestBody DataType data) {
        String JDBC_URL = "jdbc:mariadb://localhost:3306/board";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "yourPW";
        LocalDate date = LocalDate.now();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "INSERT INTO workout_table (exercise_name, sets, reps, weight, feedback, date, exercise_category, exercise_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, data.getExercise());
                pstmt.setInt(2, data.getSets());
                pstmt.setInt(3, data.getReps());
                pstmt.setInt(4, data.getWeight());
                pstmt.setString(5, data.getFeedback());
                pstmt.setDate(6, Date.valueOf(date));
                pstmt.setString(7, workoutService.getExerciseCategoryByExerciseName(data.getExercise()));
                pstmt.setInt(8, workoutService.getExerciseIdByExerciseName(data.getExercise()));

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    return "Data saved successfully!";
                } else {
                    throw new SQLException("Failed to save data to the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while saving data to the database.", e);
        }
    }

    //2024-03-18 localhost:8080/board/list 글번호와 제목 불러오기 성공(정렬은 프론트부분에서)
    //2024-04-17 제목부분 삭제 후 요일별로 표시변경 및 최신순으로 정렬, 검색부분은 굳이 필요없을거같아서 삭제해도될듯
    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        // 모든 데이터를 가져옴
        Page<WorkoutTable> resultList = workoutService.workoutRecordList(pageable);
        List<WorkoutTable> uniqueDates = resultList.stream()
                .collect(Collectors.groupingBy(WorkoutTable::getDate))
                .values().stream()
                .map(list -> list.get(0)) // 하나의 값만 유지
                .collect(Collectors.toList());

        // uniqueDates를 최신 날짜 순으로 정렬
        Collections.sort(uniqueDates, (a, b) -> b.getDate().compareTo(a.getDate()));

        int nowPage = 0;
        int startPage = 0;
        int endPage = 0;

        if (resultList != null && !resultList.isEmpty()) {
            nowPage = resultList.getPageable().getPageNumber() + 1;
            startPage = Math.max(nowPage - 4, 1);
            endPage = Math.min(nowPage + 5, resultList.getTotalPages());
        }

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("resultList", uniqueDates);

        return "boardlist";
    }

    //view 구현완료
    @GetMapping("/board/view")
    public String boardView(Model model, @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Page<WorkoutTable> workoutTables = workoutService.workoutRecordListByDate(date, PageRequest.of(0, 10));
        model.addAttribute("workoutTables", workoutTables);
        return "boardview";
    }

    //modify 구현완료
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", workoutService.getBoardById(id));
        return "boardmodify";
    }

    @PostMapping(value = "/board/update/{id}")
    @ResponseBody
    public String boardUpdate(@PathVariable("id") Integer id, @RequestBody Map<String, Object> data){
        String JDBC_URL = "jdbc:mariadb://localhost:3306/board";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "yourPW";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "UPDATE workout_table SET sets=?, reps=?, weight=?, feedback=? WHERE record_id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, (Integer) data.get("sets"));
                pstmt.setInt(2, (Integer) data.get("reps"));
                pstmt.setInt(3, (Integer) data.get("weight"));
                pstmt.setString(4, (String) data.get("feedback"));
                pstmt.setInt(5,id);
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    return "Data modified successfully!";
                } else {
                    throw new SQLException("Failed to save data to the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while saving data to the database.", e);
        }
    }

    // delete function(Completed in 2024-04-17)
    @PostMapping("/board/kill/{id}")
    public String boardDelete(@PathVariable("id") Integer id) {
        String JDBC_URL = "jdbc:mariadb://localhost:3306/board";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "yourPW";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            conn.setAutoCommit(true);
            String sql = "DELETE FROM workout_table WHERE record_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, id);
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    conn.commit();
                    return "Data Deleted!";
                } else {
                    throw new SQLException("Failed to delete data from the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while deleting data from the database.", e);
        }

    }

    @GetMapping("/board/delete/{id}")
    public String pageAfterDeleted(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", workoutService.getBoardById(id));
        return "boarddelete";
    }
}
