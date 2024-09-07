package Airdex.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "workout_table")
@Component
public class WorkoutTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "record_id")
    private Integer record_id;

    @Column(name = "exercise_id")
    private Long exerciseId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "reps")
    private int reps;

    @Column(name = "sets")
    private int sets;

    @Column(name = "weight")
    private int weight;

    @Column(name = "feedback")
    private String feedback;


    @JoinColumn(name = "exercise_category")
    private String exerciseCategory;

    @JoinColumn(name = "exercise_name")
    private String exercise_name;

    //제목은 직접 입력받고, 내용부분에는 운동부위를 표시하도록 수정
    public String getExerciseName() {
        return exercise_name;
    }
}
