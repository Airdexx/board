package Airdex.board.repository;

import Airdex.board.entity.WorkoutTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface WorkoutTableRepository extends JpaRepository<WorkoutTable, Integer> { // 두번째 Integer 부분은 첫번째 매개변수의 기본키와 같은속성
    Page<WorkoutTable> findByExerciseCategoryContaining(String exercise_category, Pageable pageable);

    Page<WorkoutTable> findByDate(LocalDate date, Pageable pageable);

}