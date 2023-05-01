package com.example.demo.config;


import java.util.logging.Logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class CustomLogger{
    private static final Logger logger = Logger.getLogger("MyCustomLogger");

    static{
        logger.setLevel(Level.ALL);

        try{
            FileHandler fileHandler = new FileHandler("C:/Users/ioana.LAPTOP-4D7BN58Q/IdeaProjects/demo/src/main/resources/logger/my_logs.log");
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch(IOException e) {
            logger.log(Level.SEVERE, "Error creating file handler", e);
        }
    }

    public static void logInfo(String message){
        logger.log(Level.INFO, message);
    }

    public static void logWarning(String message){
        logger.log(Level.WARNING, message);
    }

    public static void logError(String message, Throwable throwable){
        logger.log(Level.SEVERE, message, throwable);
    }
}
