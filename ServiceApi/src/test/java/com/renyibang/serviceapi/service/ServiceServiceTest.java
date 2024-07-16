package com.renyibang.serviceapi.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.client.UserClient;
import com.renyibang.global.dto.ServiceDTO;
import com.renyibang.serviceapi.dao.ServiceCommentDao;
import com.renyibang.serviceapi.dao.ServiceDao;
import com.renyibang.serviceapi.dao.ServiceMessageDao;
import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.entity.ServiceComment;
import com.renyibang.serviceapi.entity.ServiceMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ServiceServiceTest {
    private MockMvc mockMvc;

    @Mock
    private ServiceDao serviceDao;

    @Mock
    private ServiceCommentDao serviceCommentDao;

    @Mock
    private ServiceMessageDao serviceMessageDao;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private ServiceService serviceService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(serviceService).build();
    }

    @Test
    public void testGetServiceByPaging() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", 1);
        jsonObject.put("size", 10);
        jsonObject.put("serviceType", 1);
        jsonObject.put("serviceStatus", 1);
        jsonObject.put("serviceTitle", "test");
        serviceService.getServiceByPaging(jsonObject);
    }
}
