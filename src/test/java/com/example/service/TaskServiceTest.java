package com.example.service;

import com.example.model.Task;
import com.example.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    private TaskService service;

    @BeforeEach
    void setUp() {
        service = new TaskService(new TaskRepository());
    }

    @Test
    void createAndRetrieveTask() {
        Task task = new Task(null, "Test task", false);
        Task created = service.createTask(task);
        assertNotNull(created.getId());
        assertEquals("Test task", created.getTitle());
        assertFalse(created.isCompleted());
    }

    @Test
    void getAllTasks() {
        service.createTask(new Task(null, "Task 1", false));
        service.createTask(new Task(null, "Task 2", false));
        List<Task> tasks = service.getAllTasks();
        assertEquals(2, tasks.size());
    }

    @Test
    void updateTask() {
        Task created = service.createTask(new Task(null, "Original", false));
        Task updated = service.updateTask(created.getId(), new Task(null, "Updated", true));
        assertEquals("Updated", updated.getTitle());
        assertTrue(updated.isCompleted());
    }

    @Test
    void deleteTask() {
        Task created = service.createTask(new Task(null, "To delete", false));
        service.deleteTask(created.getId());
        assertThrows(RuntimeException.class, () -> service.getTaskById(created.getId()));
    }
}
