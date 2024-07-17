package com.renyibang.taskapi.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.util.ResponseUtil;
import com.renyibang.taskapi.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class TaskControllerTest {
  private MockMvc mockMvc;
  @InjectMocks private TaskController taskController;
  @Mock private TaskService taskService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
  }

  @Test
  public void searchTaskTest1() throws Exception {
    when(taskService.searchTaskByPaging(
            anyString(), any(), anyString(), anyString(), anyLong(), anyLong()))
        .thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/search")
                .param("keyword", "test")
                .param("pageSize", "1")
                .param("pageIndex", "1")
                .param("order", "time")
                .param("timeBegin", "")
                .param("timeEnd", "")
                .param("priceLow", "0")
                .param("priceHigh", "-1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void searchTaskTest2() throws Exception {
    when(taskService.searchTaskByPaging(
            anyString(), any(), anyString(), anyString(), anyLong(), anyLong()))
        .thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/search")
                .param("keyword", "test")
                .param("pageSize", "1")
                .param("pageIndex", "1")
                .param("order", "rating")
                .param("timeBegin", "")
                .param("timeEnd", "")
                .param("priceLow", "0")
                .param("priceHigh", "-1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void searchTaskTest3() throws Exception {
    when(taskService.searchTaskByPaging(
            anyString(), any(), anyString(), anyString(), anyLong(), anyLong()))
        .thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/search")
                .param("keyword", "test")
                .param("pageSize", "1")
                .param("pageIndex", "1")
                .param("order", "other")
                .param("timeBegin", "")
                .param("timeEnd", "")
                .param("priceLow", "0")
                .param("priceHigh", "-1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(false));
  }

  @Test
  public void searchTaskTest4() throws Exception {
    when(taskService.searchTaskByPaging(
            anyString(), any(), anyString(), anyString(), anyLong(), anyLong()))
        .thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/search")
                .param("keyword", "test")
                .param("pageSize", "0")
                .param("pageIndex", "1")
                .param("order", "time")
                .param("timeBegin", "")
                .param("timeEnd", "")
                .param("priceLow", "0")
                .param("priceHigh", "-1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(false));
  }

  @Test
  public void searchTaskTest5() throws Exception {
    when(taskService.searchTaskByPaging(
            anyString(), any(), anyString(), anyString(), anyLong(), anyLong()))
        .thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/search")
                .param("keyword", "test")
                .param("pageSize", "1")
                .param("pageIndex", "-1")
                .param("order", "time")
                .param("timeBegin", "")
                .param("timeEnd", "")
                .param("priceLow", "0")
                .param("priceHigh", "-1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(false));
  }

  @Test
  public void getTaskInfoTest() throws Exception {
    when(taskService.getTaskInfo(anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(get("/api/task/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getTaskCommentTest1() throws Exception {
    when(taskService.getTaskComments(anyLong(), any())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/1/comment")
                .param("pageSize", "1")
                .param("pageIndex", "1")
                .param("order", "likes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getTaskCommentTest2() throws Exception {
    when(taskService.getTaskComments(anyLong(), any())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/1/comment")
                .param("pageSize", "1")
                .param("pageIndex", "1")
                .param("order", "time"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getTaskCommentTest3() throws Exception {
    when(taskService.getTaskComments(anyLong(), any())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/1/comment")
                .param("pageSize", "1")
                .param("pageIndex", "1")
                .param("order", "other"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(false));
  }

  @Test
  public void getTaskCommentTest4() throws Exception {
    when(taskService.getTaskComments(anyLong(), any())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/1/comment")
                .param("pageSize", "0")
                .param("pageIndex", "1")
                .param("order", "likes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(false));
  }

  @Test
  public void getTaskCommentTest5() throws Exception {
    when(taskService.getTaskComments(anyLong(), any())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/1/comment")
                .param("pageSize", "1")
                .param("pageIndex", "-1")
                .param("order", "likes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(false));
  }

  @Test
  public void getTaskMessageTest1() throws Exception {
    when(taskService.getTaskMessages(anyLong(), any())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/1/message")
                .param("pageSize", "1")
                .param("pageIndex", "1")
                .param("order", "likes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getTaskMessageTest2() throws Exception {
    when(taskService.getTaskMessages(anyLong(), any())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/1/message")
                .param("pageSize", "1")
                .param("pageIndex", "1")
                .param("order", "time"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getTaskMessageTest3() throws Exception {
    when(taskService.getTaskMessages(anyLong(), any())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/1/message")
                .param("pageSize", "1")
                .param("pageIndex", "1")
                .param("order", "other"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(false));
  }

  @Test
  public void getTaskMessageTest4() throws Exception {
    when(taskService.getTaskMessages(anyLong(), any())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/1/message")
                .param("pageSize", "0")
                .param("pageIndex", "1")
                .param("order", "likes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(false));
  }

  @Test
  public void getTaskMessageTest5() throws Exception {
    when(taskService.getTaskMessages(anyLong(), any())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            get("/api/task/1/message")
                .param("pageSize", "1")
                .param("pageIndex", "-1")
                .param("order", "likes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(false));
  }

  @Test
  public void likeCommentTest() throws Exception {
    when(taskService.likeComment(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(put("/api/task/comment/1/like").header("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void unlikeCommentTest() throws Exception {
    when(taskService.unlikeComment(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(delete("/api/task/comment/1/unlike").header("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void likeMessageTest() throws Exception {
    when(taskService.likeMessage(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(put("/api/task/message/1/like").header("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void unlikeMessageTest() throws Exception {
    when(taskService.unlikeMessage(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(delete("/api/task/message/1/unlike").header("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void collectTaskTest() throws Exception {
    when(taskService.collectTask(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(put("/api/task/1/collect").header("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void uncollectTaskTest() throws Exception {
    when(taskService.uncollectTask(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(delete("/api/task/1/uncollect").header("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void accessTaskTest() throws Exception {
    when(taskService.accessTask(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(put("/api/task/1/access").header("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void unaccessTaskTest() throws Exception {
    when(taskService.unaccessTask(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(delete("/api/task/1/unaccess").header("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void publishMessageTest() throws Exception {
    when(taskService.publishMessage(anyLong(), anyLong(), any(JSONObject.class)))
        .thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            put("/api/task/1/message")
                .header("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void deleteMessageTest() throws Exception {
    when(taskService.deleteMessage(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(delete("/api/task/message/1").header("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void publishCommentTest() throws Exception {
    when(taskService.publishComment(anyLong(), anyLong(), any(JSONObject.class)))
        .thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            put("/api/task/1/comment")
                .header("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void deleteCommentTest() throws Exception {
    when(taskService.deleteComment(anyLong(), anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(delete("/api/task/comment/1").header("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void publishTaskTest() throws Exception {
    when(taskService.publishTask(anyLong(), any(JSONObject.class)))
        .thenReturn(ResponseUtil.success());
    mockMvc
        .perform(
            post("/api/task/issue")
                .header("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getTaskDtoByIdTest() throws Exception {
    when(taskService.getTaskDtoById(anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(get("/api/task/getTask/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getTaskOwnerIdTest() throws Exception {
    when(taskService.getTaskOwnerId(anyLong())).thenReturn(ResponseUtil.success());
    mockMvc
        .perform(get("/api/task/1/ownerId"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }
}
