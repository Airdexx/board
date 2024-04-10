package Airdex.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@Data
@Table(name = "workout")
@Component
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "exercise_id")
    private Long exerciseId;

    @Column(name = "exercise_name")
    private String exercise_name;

    @Column(name = "exercise_category")
    private String exerciseCategory;

    @Column(name = "exercise_description")
    private String exercise_description;

}