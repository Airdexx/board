package Airdex.board.service;

import Airdex.board.entity.Workout;
import Airdex.board.entity.WorkoutTable;
import Airdex.board.repository.WorkoutRepository;
import Airdex.board.repository.WorkoutTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@ComponentScan(basePackages = "Airdex.board")

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private WorkoutTableRepository workoutTableRepository;

    // 글 작성 처리
    public void writeWorkoutRecord(WorkoutTable workoutTable) {
        // 운동 기록 저장하는 로직
        workoutTableRepository.save(workoutTable);
    }

    // 전체 운동 기록 리스트 처리
    public Page<WorkoutTable> workoutRecordList(Pageable pageable) {
        return workoutTableRepository.findAll(pageable);
    }

    // 특정 부위를 포함한 운동 기록 리스트 처리
    public Page<WorkoutTable> workoutRecordListByExercisePartContaining(String exercise_category, Pageable pageable) {
        return workoutTableRepository.findByExerciseCategoryContaining(exercise_category, pageable);
    }

    // 특정 날짜의 운동 기록 리스트 처리
    public Page<WorkoutTable> workoutRecordListByDate(LocalDate date, Pageable pageable) {
        return workoutTableRepository.findByDate(date, pageable);
    }

    // 글 조회 메서드
    public WorkoutTable getBoardById(Integer id) {
        return workoutTableRepository.findById(id).orElse(null);
    }

    // 중분류에 들어갈 운동종류 받아올 배열 선언
    List<String> allBodyPart = Arrays.asList(new String[]{"등", "가슴", "어깨", "하체", "팔"});

    List<String> back_name = new ArrayList<>();
    List<String> chest_name = new ArrayList<>();
    List<String> delt_name = new ArrayList<>();
    List<String> leg_name = new ArrayList<>();
    List<String> arm_name = new ArrayList<>();

    //exerciseId 최대값 반환
    public Long getMaxId() {
        Workout workout = workoutRepository.findTopByOrderByExerciseIdDesc();
        if (workout != null) {
            return workout.getExerciseId();
        }
        return null;
    }

    // 운동부위별 분류 메서드
    public List<List<String>> sortByCategory(Workout workout) {
        List<List<String>> result = new ArrayList<>();
        String url = "jdbc:mariadb://localhost:3306/board";
        String user = "root";
        String password = "yourPW";

        // 각 운동 부위에 해당하는 리스트를 메서드 호출마다 초기화
        List<String> back_name = new ArrayList<>();
        List<String> chest_name = new ArrayList<>();
        List<String> delt_name = new ArrayList<>();
        List<String> leg_name = new ArrayList<>();
        List<String> arm_name = new ArrayList<>();
        long i = 1;

        while (i <= getMaxId()) {
            //jdbc 연결
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                String sql = "SELECT exercise_name, exercise_category FROM workout WHERE exercise_id = ?";

                // PreparedStatement 생성
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, (int) i); // 조건에 맞는 exercise_id 값을 설정

                //쿼리 실행 및 결과 처리
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String exerciseName = rs.getString("exercise_name");
                    String category = rs.getString("exercise_category");
                    switch (category) {
                        case "등":
                            back_name.add(exerciseName);
                            break;
                        case "가슴":
                            chest_name.add(exerciseName);
                            break;
                        case "어깨":
                            delt_name.add(exerciseName);
                            break;
                        case "하체":
                            leg_name.add(exerciseName);
                            break;
                        case "팔":
                            arm_name.add(exerciseName);
                            break;
                    }
                }
                i++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 각 운동 부위에 해당하는 리스트를 결과 리스트에 추가
        result.add(back_name);
        result.add(chest_name);
        result.add(delt_name);
        result.add(leg_name);
        result.add(arm_name);
        return result;
    }
    public String getExerciseCategoryByExerciseName(String usrExerciseName){
        String JDBC_URL = "jdbc:mariadb://localhost:3306/board";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "Airdex3412@";
        String exerciseCategory = null;
        String sql = "SELECT exercise_category FROM workout WHERE exercise_name = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usrExerciseName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                exerciseCategory = rs.getString("exercise_category");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 예외 처리
        }

        return exerciseCategory;
    }

    public int getExerciseIdByExerciseName(String exerciseName){

        String JDBC_URL = "jdbc:mariadb://localhost:3306/board";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "Airdex3412@";
        int exerciseId = -1;
        String sql = "SELECT exercise_id FROM workout WHERE exercise_name = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, exerciseName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                exerciseId = rs.getInt("exercise_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 예외 처리
        }

        return exerciseId;
        }
    }
    /* 서브렛 및 드롭다운으로 입력받은 세부운동을 반복문을 이용해 db의 exercise_name과 비교해
        같은 값의 exercise_category값 반환받은다음 새 변수로 할당받고(스프링부트 부분에서)
        exercise_id값 또한 return받고 그 값을 서브렛에 같이 pstmt부분에 작성해서 전송(이건 Controller4Servlet에 코드 있음)
        Controller4Servlet에 exercise를 받아오는 부분이 있어서 getBodyPart부분 Controller4Servlet부분으로 옮기면 될듯
     */
    /*
    public String getBodyPart(Workout workout) {
        // connect to DB
        String url = "jdbc:mariadb://localhost:3306/board";
        String user = "root";
        String password = "Airdex3412@";

        long j = 1;
        while (j <= getMaxId()) {
            //jdbc 연결
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                String sql = "SELECT exercise_name, exercise_category FROM workout WHERE exercise_id = ?";

                // PreparedStatement 생성
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, (int) j); // 조건에 맞는 exercise_id 값을 설정

                //쿼리 실행 및 결과 처리
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String exerciseName = rs.getString("exercise_name");
                    String category = rs.getString("exercise_category");
                    if(exerciseName)
                }
                j++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
     */
