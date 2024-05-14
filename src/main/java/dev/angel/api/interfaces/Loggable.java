package dev.angel.api.interfaces;

import org.apache.logging.log4j.Level;

public interface Loggable {
    void log(String text);

    void log(String text, int id);

    void log(String text, boolean delete);

    void log(Level level, String text);
}