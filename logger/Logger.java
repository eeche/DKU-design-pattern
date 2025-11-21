package logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logger {

    // ---- Singleton ----
    private static final Logger INSTANCE = new Logger();

    private Logger() { }

    public static Logger getInstance() {
        return INSTANCE;
    }
    // --------------------

    private final List<String> logs = new ArrayList<>();
    private final DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 내부에서 로그 한 줄을 실제 출력하고 저장
     */
    public void log(LogLevel level, String msg) {
        String line = String.format(
                "[%s] [%s] %s",
                LocalDateTime.now().format(fmt),
                level,
                msg
        );

        // 콘솔 출력
        System.out.println(line);

        // 메모리에 로그 저장
        logs.add(line);
    }

    // INFO 편의 메서드
    public void info(String msg) {
        log(LogLevel.INFO, msg);
    }

    // WARN 편의 메서드
    public void warn(String msg) {
        log(LogLevel.WARN, msg);
    }

    // ERROR 편의 메서드
    public void error(String msg) {
        log(LogLevel.ERROR, msg);
    }

    /**
     * 외부에서 로그 리스트 조회
     */
    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }
}
