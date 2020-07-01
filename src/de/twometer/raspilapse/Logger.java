package de.twometer.raspilapse;

public class Logger {

    private static void log(String prefix, String message) {
        System.out.printf("[%s] %s\n", prefix, message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    public static void info(String message){
        log("INFO", message);
    }

}
