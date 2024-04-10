package Airdex.board.entity;

public class DataType extends WorkoutTable{
    private String exercise;
    private int sets;
    private int reps;
    private int weight;
    private String title;
    private String feedback;

    private int record_id;
    public String getExercise() {
        return exercise;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public int getWeight() {
        return weight;
    }

    public String getFeedback() {
        return feedback;
    }
    public int getRecordid() {return record_id;}

    public String getTitle() {
        return title;
    }
}
