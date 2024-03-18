package main.models;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String title, String description, int id, Status status, int epicId) {
        super(title, description, id, status);
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }



    @Override
    public String toString() {
        return "main.models.Subtask{" +
                "epicId=" + epicId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public String toStringForFile(){
        return String.format("%d,SUBTASK,%s,%s,%s,", id, title, status, description, epicId);
    }
}
