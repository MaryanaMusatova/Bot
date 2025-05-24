package pro.sky.telegrambot.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Task;
import pro.sky.telegrambot.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final TaskRepository repository;
    private final TelegramBot bot;

    @Scheduled(cron = "${notification.check.interval}")
    public void scheduleSendTask() {
        log.info("Запуск проверки задач для отправки");
        List<Task> tasks = getTasks();
        tasks.forEach(task -> {
            log.info("Отправка напоминания для чата {}: {}", task.getChatId(), task.getMessage());
            bot.execute(new SendMessage(task.getChatId(), "Напоминание: " + task.getMessage()));
        });
    }

    @Scheduled(cron = "59 59 23 * * *")
    @Transactional
    public void scheduleCleanTask() {
        log.info("Запуск очистки старых задач");
        repository.deleteByNotificationTimeBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    private List<Task> getTasks() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        return repository.findByNotificationTime(now);
    }
}