import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{
    private ArrayList<Subtask> subtasks;

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public Epic(String title, String description, int id, Status status, ArrayList<Subtask> subtasks) {
        super(title, description, id, status);
        this.subtasks = subtasks;
    }
}
