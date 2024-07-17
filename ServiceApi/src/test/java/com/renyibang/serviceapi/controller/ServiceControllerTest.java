package com.renyibang.serviceapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.dto.ServiceDTO;
import com.renyibang.global.dto.TaskDTO;
import com.renyibang.global.dto.UserDTO;
import com.renyibang.global.util.ResponseUtil;
import com.renyibang.serviceapi.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ServiceControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ServiceService serviceService;

    @InjectMocks
    private ServiceController serviceController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(serviceController).build();
    }

    @Test
    public void testSearchServiceWithTimeOrder() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.searchServiceByPaging(anyString(), any(Pageable.class), anyString(), anyString(), anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(get("/api/service/search")
                        .param("keyword", "test")
                        .param("pageSize", "10")
                        .param("pageIndex", "0")
                        .param("order", "time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testSearchServiceWithRatingOrder() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.searchServiceByPaging(anyString(), any(Pageable.class), anyString(), anyString(), anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(get("/api/service/search")
                        .param("keyword", "test")
                        .param("pageSize", "10")
                        .param("pageIndex", "0")
                        .param("order", "rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testSearchServiceWithUndefinedOrder() throws Exception {
        // 执行测试
        mockMvc.perform(get("/api/service/search")
                        .param("keyword", "test")
                        .param("pageSize", "10")
                        .param("pageIndex", "0")
                        .param("order", "undefined"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void testGetServiceInfo() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.getServiceInfo(anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(get("/api/service/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testGetServiceCommentWithLikesOrder() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.getServiceComments(anyLong(), any(Pageable.class))).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(get("/api/service/1/comment")
                        .param("pageSize", "10")
                        .param("pageIndex", "0")
                        .param("order", "likes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testGetServiceCommentWithTimeOrder() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.getServiceComments(anyLong(), any(Pageable.class))).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(get("/api/service/1/comment")
                        .param("pageSize", "10")
                        .param("pageIndex", "0")
                        .param("order", "time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testGetServiceCommentWithUndefinedOrder() throws Exception {
        // 执行测试
        mockMvc.perform(get("/api/service/1/comment")
                        .param("pageSize", "10")
                        .param("pageIndex", "0")
                        .param("order", "undefined"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void testGetServiceMessageWithLikesOrder() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.getServiceMessages(anyLong(), any(Pageable.class))).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(get("/api/service/1/message")
                        .param("pageSize", "10")
                        .param("pageIndex", "0")
                        .param("order", "likes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testGetServiceMessageWithTimeOrder() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.getServiceMessages(anyLong(), any(Pageable.class))).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(get("/api/service/1/message")
                        .param("pageSize", "10")
                        .param("pageIndex", "0")
                        .param("order", "time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testGetServiceMessageWithUndefinedOrder() throws Exception {
        // 执行测试
        mockMvc.perform(get("/api/service/1/message")
                        .param("pageSize", "10")
                        .param("pageIndex", "0")
                        .param("order", "undefined"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false));
    }

    @Test
    public void testLikeComment() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.likeComment(anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service/comment/1/like"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testUnlikeComment() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.unlikeComment(anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service/comment/1/unlike"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testLikeMessage() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.likeMessage(anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service/message/1/like"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testUnlikeMessage() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.unlikeMessage(anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service/message/1/unlike"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testCollectService() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.collectService(anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service/1/collect"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testUncollectService() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.uncollectService(anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service/1/uncollect"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testAccessService() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.accessService(anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service/1/access")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testUnaccessService() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.unaccessService(anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service/1/unaccess")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testPublishMessage() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.publishMessage(anyLong(), anyLong(), any(JSONObject.class))).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service/1/message")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.deleteMessage(anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(get("/api/service/message/1")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testPublishComment() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.publishComment(anyLong(), anyLong(), any(JSONObject.class))).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service/1/comment")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testDeleteComment() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        when(serviceService.deleteComment(anyLong(), anyLong())).thenReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(get("/api/service/comment/1")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }

    @Test
    public void testPublishService() throws Exception {
        // 假设的返回值
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("success", true);
        // 配置模拟对象的行为
        given(serviceService.publishService(anyLong(), any(JSONObject.class))).willReturn(expectedResponse);
        // 执行测试
        mockMvc.perform(put("/api/service")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));
    }
}
