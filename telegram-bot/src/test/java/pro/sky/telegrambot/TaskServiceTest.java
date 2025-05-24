package pro.sky.telegrambot;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.entity.Task;
import pro.sky.telegrambot.repository.TaskRepository;
import pro.sky.telegrambot.service.TaskService;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void save_ValidInput_SavesTask() {

        Long chatId = 123L;
        String message = "01.01.2025 20:00 Тестовое напоминание";


        taskService.save(chatId, message);


        verify(taskRepository).save(any(Task.class));
    }
}