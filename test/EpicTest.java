package main.models;

import main.InMemoryHistoryManager;
import main.InMemoryTaskManager;
import main.Managers;
import main.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {


    private static TaskManager inMemoryTaskManager;

    @BeforeEach
    void setUp() {
        inMemoryTaskManager = Managers.getDefault();
        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1", 0, Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2", 0, Status.NEW, new ArrayList<>());
        Epic epic3 = new Epic("Эпик 3", "Описание Эпика 3", 0, Status.NEW, new ArrayList<>());

        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.addEpic(epic3);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 0, Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 0, Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", 0, Status.NEW, epic2.getId());
        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4", 0, Status.NEW, epic2.getId());
        Subtask subtask5 = new Subtask("Подзадача 5", "Описание подзадачи 5", 0, Status.NEW, epic3.getId());
        Subtask subtask6 = new Subtask("Подзадача 6", "Описание подзадачи 6", 0, Status.NEW, epic3.getId());
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        inMemoryTaskManager.addSubtask(subtask4);
        inMemoryTaskManager.addSubtask(subtask5);
        inMemoryTaskManager.addSubtask(subtask6);
    }

    @Test
    void getSubtasksIds() {
        ArrayList<Integer>  subtasks = inMemoryTaskManager.getEpicById(1).getSubtasksIds();
        ArrayList<Integer> actual = new ArrayList<>();
        actual.add(4);
        actual.add(5);
        assertEquals(subtasks, actual, "Массивы не равны");
    }

    @Test
    void setSubtasksIds() {
    }

    @Test
    void removeSubtask() {
    }

    @Test
    void addSubtaskId() {
    }

    @Test
    void clearAllSubtasks() {
    }


    @Test
    void checkEpicsWithSameIdIsEquals() {
        // проверьте, что наследники класса Task равны друг другу, если равен их id;
        Epic expected = inMemoryTaskManager.getEpicById(1);
        Epic actual = inMemoryTaskManager.getEpicById(1);
        assertEquals(expected, actual, "Не равны");

    }
}