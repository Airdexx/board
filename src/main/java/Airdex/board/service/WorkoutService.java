package Airdex.board.service;

import Airdex.board.entity.Workout;
import Airdex.board.entity.WorkoutTable;
import Airdex.board.repository.WorkoutRepository;
import Airdex.board.repository.WorkoutTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BoardService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private WorkoutTableRepository workoutTableRepository;

    // 글 작성 처리
    public void write(Workout workout) {
        // 운동 종류 저장하는 로직
        workoutRepository.save(workout);
    }

    public void writeWorkoutRecord(WorkoutTable workoutTable) {
        // 운동 기록 저장하는 로직
        workoutTableRepository.save(workoutTable);
    }

    // 전체 운동 기록 리스트 처리
    public Page<WorkoutTable> workoutRecordList(Pageable pageable) {
        return workoutTableRepository.findAll(pageable);
    }

    // 특정 단어를 포함한 운동 기록 리스트 처리
    public Page<WorkoutTable> workoutRecordListByTitleContaining(String searchKeyword, Pageable pageable) {
        return workoutTableRepository.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 날짜의 운동 기록 리스트 처리
    public Page<WorkoutTable> workoutRecordListByDate(LocalDateTime date, Pageable pageable) {
        return workoutTableRepository.findByDate(date, pageable);
    }
}