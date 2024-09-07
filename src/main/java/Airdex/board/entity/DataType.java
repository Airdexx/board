package Airdex.board.entity;

public class DataType{
    private String exercise;
    private int sets;
    private int reps;
    private int weight;
    private String feedback;
    
    public DataType() {
    }
    public DataType(String exercise,int sets,int reps, int weight, String feedback){
        this.exercise =exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.feedback = feedback;
    }
    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}