package com.example.demo.service;

import com.example.demo.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatusService {

    private final StatusRepository repository;


    public StatusService(StatusRepository repository) {
        this.repository = repository;
    }

    public List<String> getStatusList() {
        return repository.findAll()
                .stream().map(status -> status.getName()).collect(Collectors.toList());
    }
}
