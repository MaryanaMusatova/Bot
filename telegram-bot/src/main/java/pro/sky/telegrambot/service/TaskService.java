package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Task;
import pro.sky.telegrambot.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})\\s+(.+)");

    public void save(Long chatId, String message) {
        log.info("Сохранение задачи для чата {}: {}", chatId, message);
        Task task = parseMessage(chatId, message);
        if (task != null) {
            taskRepository.save(task);
            log.info("Задача сохранена: {}", task);
        } else {
            log.warn("Не удалось распарсить сообщение: {}", message);
        }
    }

    private Task parseMessage(Long chatId, String input) {
        Matcher matcher = MESSAGE_PATTERN.matcher(input);
        if (matcher.matches()) {
            LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
            String taskMessage = matcher.group(2);

            Task task = new Task();
            task.setChatId(chatId);
            task.setMessage(taskMessage);
            task.setNotificationTime(dateTime);

            return task;
        }
        return null;
    }
}