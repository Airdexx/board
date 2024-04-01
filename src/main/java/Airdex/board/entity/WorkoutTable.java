package Airdex.board.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "workout_table")
public class workout_table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "record_id")
    private Integer record_id;

    @Column(name = "exercise_id")
    private Long exercise_id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "reps")
    private int reps;

    @Column(name = "sets")
    private int sets;

    @Column(name = "weight")
    private int weight;

    @Column(name ="feedback")
    private String feedback;
}