package main;


import main.models.Epic;
import main.models.Status;
import main.models.Subtask;
import main.models.Task;

public class FileBackedTaskManager extends InMemoryTaskManager  {
    private String autoSaveFileName = new String();
    public FileBackedTaskManager(HistoryManager historyManager, String filename) {
        super(historyManager);
        autoSaveFileName = filename;
    }

    public void save()
    {
        //pass
    }

    @Override
    public void deleteTasks(){
        super.deleteTasks();
        save();
    }
    @Override
    public void addTask(Task newTask) {
        super.addTask(newTask);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void changeTaskStatus(Task task, Status status) {
        super.changeTaskStatus(task, status);
        save();
    }

    @Override
    public void deleteSubtasks(){
        super.deleteSubtasks();
        save();
    }

    @Override
    public void addSubtask(Subtask newSubtask) {
        super.addTask(newSubtask);
        save();
    }
    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }
    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void  changeSubtaskStatus(Subtask subtask, Status status){
        super.changeSubtaskStatus(subtask, status);
        save();
    }

    @Override
    public void deleteEpics(){
        super.deleteEpics();
        save();
    }

    @Override
    public void addEpic(Epic newEpic) {
        super.addEpic(newEpic);
        save();
    }
    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void removeEpicSubtask(Epic epic, int id) {
        super.removeEpicSubtask(epic, id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic){
        super.updateEpicStatus(epic);
        save();
    }

}
