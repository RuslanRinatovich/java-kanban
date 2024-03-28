package main.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected Status status;
    protected long duration;
    protected LocalDateTime startTime;

    public Task(int id, String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = duration.toMinutes();
        this.startTime = startTime;

    }

    public Task(String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = 0;
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = duration.toMinutes();
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return Duration.ofMinutes(duration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration.toMinutes();
    }

    public  LocalDateTime getEndTime()
    {
        return this.startTime.plusMinutes(this.duration);
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }

    @Override
    public String toString() {
        return "main.models.Task{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", id=" + id + ", status=" + status + '}';
    }

    public String toStringForFile() {
        return String.format("%d,TASK,%s,%s,%s,", id, title, status, description);
    }
}
