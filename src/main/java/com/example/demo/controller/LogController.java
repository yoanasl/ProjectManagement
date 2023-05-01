package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LogController{
    @GetMapping("/logs")
    public String logs(Model model) throws IOException{
        List<String> logs = new ArrayList<>();

        Path logFilePath = Paths.get("C:\\Users\\marin\\Downloads\\ProjectManagement\\src\\main\\resources\\logger\\my_logs.log");
        List<String> logLines = Files.readAllLines(logFilePath);

        for(int i = 0; i < logLines.size(); i += 2){
            String logLine1 = logLines.get(i);
            String logLine2 = i + 1 < logLines.size() ? logLines.get(i + 1) : null;

            String logMessage1 = (logLine1 + " " + logLine2);

            if(logMessage1 != null){
                logs.add(logMessage1);
            }

        }
        model.addAttribute("logs", logs);
        return "log";
    }


}
