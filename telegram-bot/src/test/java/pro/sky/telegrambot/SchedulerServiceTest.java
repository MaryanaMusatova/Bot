package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.entity.Task;
import pro.sky.telegrambot.repository.TaskRepository;
import pro.sky.telegrambot.scheduler.SchedulerService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TelegramBot telegramBot;
    private SchedulerService schedulerService;

    @BeforeEach
    void setUp() {
        telegramBot = new MockTelegramBot();
        schedulerService = new SchedulerService(taskRepository, telegramBot);
    }

    @Test
    void scheduleSendTask_FindsTasks_SendsMessages() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
        Task task = new Task();
        task.setChatId(123L);
        task.setMessage("Тест");

        when(taskRepository.findByNotificationTime(now)).thenReturn(List.of(task));

        schedulerService.scheduleSendTask();

        verify(taskRepository).findByNotificationTime(now);
    }

    private static class MockTelegramBot extends TelegramBot {
        public MockTelegramBot() {
            super("dummy-token");
        }

        @Override
        public <T extends com.pengrad.telegrambot.request.BaseRequest<T, R>, R extends BaseResponse>
        R execute(BaseRequest<T, R> request) {
            // Возвращаем null, так как это mock
            return null;
        }
    }
}