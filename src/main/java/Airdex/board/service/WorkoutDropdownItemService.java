package Airdex.board.service;

import Airdex.board.entity.WorkoutTable;
import Airdex.board.repository.WorkoutTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkoutDropdownItemService{
    @Autowired
    private WorkoutTableRepository workoutTableRepository;
    public List<WorkoutTable> getAllPart(){
        return workoutTableRepository.findAll();
    }
}