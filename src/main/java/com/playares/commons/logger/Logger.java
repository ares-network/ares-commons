package com.playares.commons.logger;

import org.bukkit.Bukkit;
import java.util.logging.Level;

public final class Logger {
    /**
     * Print a simple message to console
     *
     * Does not write to file
     * @param message Message
     */
    public static void print(String message) {
        Bukkit.getLogger().log(Level.INFO, message);
    }

    /**
     * Print a warning message to console and write to file
     * @param message Message
     */
    public static void warn(String message) {
        Bukkit.getLogger().log(Level.WARNING, message);
    }

    /**
     * Print an error message to console and write to file
     * @param message Message
     */
    public static void error(String message) {
        Bukkit.getLogger().log(Level.SEVERE, message);
    }
}