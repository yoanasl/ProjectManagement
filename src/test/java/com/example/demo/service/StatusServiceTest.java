package com.example.demo.service;

import com.example.demo.entity.Status;
import com.example.demo.repository.StatusRepository;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
    public class StatusServiceTest {

        @Mock
        private StatusRepository statusRepository;

        @InjectMocks
        private StatusService statusService;

        @Before
        public void setUp() throws Exception {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testGetStatusList() {
            List<Status> statusList = new ArrayList<>();
            statusList.add(new Status(1, "Status A"));
            statusList.add(new Status(2, "Status B"));
            when(statusRepository.findAll()).thenReturn(statusList);

            List<String> expected = new ArrayList<>();
            expected.add("Status A");
            expected.add("Status B");

            List<String> actual = statusService.getStatusList();

            assertEquals(expected, actual);
        }

}