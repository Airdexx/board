package Airdex.board.repository;

import Airdex.board.entity.workout_table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface workout_tableRepository extends JpaRepository<workout_table, Integer> {

    Page<workout_table> findByTitleContaining(String searchKeyword, Pageable pageable);
}