import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
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

    public void clearAllSubtasks()
    {
        this.subtasksIds.clear();
    }
}
