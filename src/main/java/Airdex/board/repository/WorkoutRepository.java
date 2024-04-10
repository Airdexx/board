package Airdex.board.repository;
import Airdex.board.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
    Workout findTopByOrderByExerciseIdDesc(); //workout 객체 반환
}
