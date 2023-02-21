import java.time.LocalDateTime;

public class Log {
    private String module;
    private LogType type;
    private LocalDateTime time;
    private String text;
    private String remoteAddress;

    private static String separator=":.:";

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(getModule());
        resultBuilder.append(separator);
        resultBuilder.append(getType());
        resultBuilder.append(separator);
        resultBuilder.append(getTime());
        resultBuilder.append(separator);
        resultBuilder.append(getRemoteAddress());
        resultBuilder.append(separator);
        resultBuilder.append(getText());
        return resultBuilder.toString();
    }

    public static Log parse(String input, String remoteAddress) {
        try {
            String[] parts = input.split(separator);
            Log log = new Log();
            log.setModule(parts[0]);
            log.setType(LogType.valueOf(parts[1]));
            log.setTime(LocalDateTime.now());
            log.setRemoteAddress(remoteAddress);
            log.setText(parts[2]);
            return log;

        } catch (Exception ex) {
            return null;
        }
    }

    public static Log parse(String input) {
        try {
            String[] parts = input.split(separator);
            Log log = new Log();
            log.setModule(parts[0]);
            log.setType(LogType.valueOf(parts[1]));
            log.setTime(LocalDateTime.parse(parts[2]));
            log.setRemoteAddress(parts[3]);
            log.setText(parts[4]);
            return log;
        } catch (Exception ex) {
            return null;
        }
    }
}
