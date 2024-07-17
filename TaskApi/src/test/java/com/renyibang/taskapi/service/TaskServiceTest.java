package com.renyibang.taskapi.service;

import com.renyibang.taskapi.dao.TaskDao;
import com.renyibang.taskapi.service.serviceImpl.TaskServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {
  @InjectMocks private TaskServiceImpl taskService;
  @Mock private TaskDao taskDao;
}
