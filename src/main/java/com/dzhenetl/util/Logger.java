package com.dzhenetl.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static Logger INSTANCE;
    private final File logFile;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private Logger() {
        logFile = new File(PropertiesUtil.get("log.path"));
    }

    public static Logger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Logger();
        }
        return INSTANCE;
    }

    public void log(String msg) {
        String time = LocalDateTime.now().format(formatter);
        String line = String.format("[%s] %s\n", time, msg);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(line);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
