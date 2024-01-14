package main.models;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }


    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    private ArrayList<Integer> subtasksIds;

    public Epic(String title, String description, int id, Status status, ArrayList<Integer> subtasksIds) {
        super(title, description, id, status);
        this.subtasksIds = subtasksIds;
    }

    public boolean removeSubtask(int id) {
        if (!this.subtasksIds.contains(id))
            return false;
        int index = this.subtasksIds.indexOf(id);
        this.subtasksIds.remove(index);
        return true;

    }

    public void addSubtaskId(int id)
    {
        subtasksIds.add(id);
    }

    public void clearAllSubtasks()
    {

        this.subtasksIds.clear();
        this.status = Status.NEW;
    }

    @Override
    public String toString() {
        return "main.models.Epic{" +
                "subtasksIds=" + subtasksIds +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
