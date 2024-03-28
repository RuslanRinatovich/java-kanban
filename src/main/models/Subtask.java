package main.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String title, String description, Status status, Duration duration, LocalDateTime startTime ,int epicId) {
        super(id, title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status,  Duration duration, LocalDateTime startTime, int epicId) {
        super(title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
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
    public String toStringForFile() {
        return String.format("%d,SUBTASK,%s,%s,%s,%d", id, title, status, description, epicId);
    }
}
