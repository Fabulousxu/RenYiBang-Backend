//package com.renyibang.taskapi.service;
//
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//import com.renyibang.global.client.UserClient;
//import com.renyibang.taskapi.dao.TaskDao;
//import com.renyibang.taskapi.dao.TaskMessageDao;
//import com.renyibang.taskapi.dao.TaskCommentDao;
//import com.renyibang.taskapi.entity.Task;
//import com.renyibang.taskapi.entity.TaskComment;
//import com.renyibang.taskapi.entity.TaskMessage;
//import com.renyibang.taskapi.service.TaskService;
//import com.renyibang.taskapi.service.serviceImpl.TaskServiceImpl;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.data.domain.*;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.function.Function;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
//public class TaskServiceTest {
//  @InjectMocks
//  private TaskServiceImpl taskService;
//
//  @Mock
//  private TaskDao taskDao;
//
//  @Mock
//    private TaskMessageDao taskMessageDao;
//
//    @Mock
//    private TaskCommentDao taskCommentDao;
//
//    @Mock
//    private UserClient userClient;
//
//    private MockMvc mockMvc;
//
//    private Task task;
//    private Pageable pageable;
//    private DateTimeFormatter formatter;
//    private List<Task> taskList;
//    private Page<Task> taskPage;
//    private LocalDateTime begintime;
//    private LocalDateTime endtime;
//    private JSONObject successResponse;
//    private JSONObject errorResponse;
//
//    @BeforeEach
//    public void setUp() {
//        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        mockMvc = MockMvcBuilders.standaloneSetup(taskService).build();
//        task = new Task();
//        task.setOwnerId(1L);
//        task.setTitle("test");
//        task.setDescription("test");
//        task.setPrice(1000);
//        task.setCreatedAt(LocalDateTime.parse("2024-06-01 01:00:00", formatter));
//        taskList = new ArrayList<>();
//        taskList.add(task);
//        taskPage = new PageImpl<>(taskList);
//        pageable = PageRequest.of(0, 10);
//      begintime = LocalDateTime.parse("2024-06-01 00:00:00", formatter);
//      endtime = LocalDateTime.parse("2024-06-02 00:00:00", formatter);
//      successResponse = new JSONObject();
//      errorResponse = new JSONObject();
//      successResponse.put("ok", true);
//      errorResponse.put("ok", false);
//    }
//
//    @Test
//  public void testSearchTaskByPaging() {
//      JSONArray userIds = new JSONArray();
//      userIds.add(1L);
//      JSONObject userObject = new JSONObject();
//      userObject.put("ok", true);
//      userObject.put("data", userIds);
//
//      when(taskDao.searchTaskByPaging(anyString(),any(Pageable.class),any(LocalDateTime.class),any(LocalDateTime.class),anyLong(),anyLong())).thenReturn(taskPage);
//      when(userClient.getUserInfos(anyList())).thenReturn(userObject);
//
//      JSONObject result = taskService.searchTaskByPaging("test", pageable, begintime.toString(), endtime.toString(), 1L, 1000L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testSearchTaskByPagingWithEmptyUserIds() {
//      JSONArray userIds = new JSONArray();
//      JSONObject userObject = new JSONObject();
//      userObject.put("ok", true);
//      userObject.put("data", userIds);
//
//      when(taskDao.searchTaskByPaging(anyString(),any(Pageable.class),any(LocalDateTime.class),any(LocalDateTime.class),anyLong(),anyLong())).thenReturn(taskPage);
//      when(userClient.getUserInfos(anyList())).thenReturn(userObject);
//
//      JSONObject result = taskService.searchTaskByPaging("test", pageable, begintime.toString(), endtime.toString(), 1L, 1000L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testSearchTaskByPagingWithInvalidParams() {
//      JSONObject result = taskService.searchTaskByPaging("test", pageable, "invalid", "invalid", -1L, -2L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testGetTaskInfo() {
//        when(taskDao.findById(anyLong())).thenReturn(task);
//        when(userClient.getUserInfo(anyLong())).thenReturn(successResponse);
//        JSONObject result = taskService.getTaskInfo(1L);
//        assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testGetTaskInfoWithEmptyTask() {
//        when(taskDao.findById(anyLong())).thenReturn(null);
//        JSONObject result = taskService.getTaskInfo(1L);
//        assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testGetTaskInfoWithEmptyUser() {
//        when(taskDao.findById(anyLong())).thenReturn(task);
//        when(userClient.getUserInfo(anyLong())).thenReturn(errorResponse);
//        JSONObject result = taskService.getTaskInfo(1L);
//        assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testGetTaskComments() {
//      TaskComment taskComment = new TaskComment();
//      taskComment.setCommenterId(1L);
//      Page<TaskComment> taskCommentPage = new PageImpl<>(List.of(taskComment));
//      when(taskCommentDao.getTaskComments(anyLong(), any(Pageable.class))).thenReturn(taskCommentPage);
//      when(userClient.getUserInfo(anyLong())).thenReturn(successResponse);
//
//      JSONObject result = taskService.getTaskComments(1L, pageable);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testGetTaskCommentsWithEmptyComments() {
//      Page<TaskComment> taskCommentPage = new PageImpl<>(List.of());
//      when(taskCommentDao.getTaskComments(anyLong(), any(Pageable.class))).thenReturn(taskCommentPage);
//
//      JSONObject result = taskService.getTaskComments(1L, pageable);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testGetTaskCommentsWithEmptyUser() {
//      TaskComment taskComment = new TaskComment();
//      taskComment.setCommenterId(1L);
//      Page<TaskComment> taskCommentPage = new PageImpl<>(List.of(taskComment));
//      when(taskCommentDao.getTaskComments(anyLong(), any(Pageable.class))).thenReturn(taskCommentPage);
//      when(userClient.getUserInfo(anyLong())).thenReturn(errorResponse);
//
//      JSONObject result = taskService.getTaskComments(1L, pageable);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testGetTaskMessages() {
//      TaskMessage taskMessage = new TaskMessage();
//      taskMessage.setMessagerId(1L);
//      Page<TaskMessage> taskMessagePage = new PageImpl<>(List.of(taskMessage));
//      when(taskMessageDao.getTaskMessages(anyLong(), any(Pageable.class))).thenReturn(taskMessagePage);
//      when(userClient.getUserInfo(anyLong())).thenReturn(successResponse);
//
//      JSONObject result = taskService.getTaskMessages(1L, pageable);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testGetTaskMessagesWithEmptyMessages() {
//      Page<TaskMessage> taskMessagePage = new PageImpl<>(List.of());
//      when(taskMessageDao.getTaskMessages(anyLong(), any(Pageable.class))).thenReturn(taskMessagePage);
//
//      JSONObject result = taskService.getTaskMessages(1L, pageable);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testGetTaskMessagesWithEmptyUser() {
//      TaskMessage taskMessage = new TaskMessage();
//      taskMessage.setMessagerId(1L);
//      Page<TaskMessage> taskMessagePage = new PageImpl<>(List.of(taskMessage));
//      when(taskMessageDao.getTaskMessages(anyLong(), any(Pageable.class))).thenReturn(taskMessagePage);
//      when(userClient.getUserInfo(anyLong())).thenReturn(errorResponse);
//
//      JSONObject result = taskService.getTaskMessages(1L, pageable);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testLikeCommentSuccess() {
//      when(taskCommentDao.likeCommentByTaskCommentId(anyLong(),anyLong())).thenReturn("点赞成功！");
//      JSONObject result = taskService.likeComment(1L, 1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testLikeCommentFailed() {
//      when(taskCommentDao.likeCommentByTaskCommentId(anyLong(),anyLong())).thenReturn("点赞失败！");
//      JSONObject result = taskService.likeComment(1L, 1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testUnlikeCommentSuccess() {
//      when(taskCommentDao.unlikeCommentByTaskCommentId(anyLong(),anyLong())).thenReturn("取消点赞成功！");
//      JSONObject result = taskService.unlikeComment(1L, 1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testUnlikeCommentFailed() {
//      when(taskCommentDao.unlikeCommentByTaskCommentId(anyLong(),anyLong())).thenReturn("取消点赞失败！");
//      JSONObject result = taskService.unlikeComment(1L, 1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testLikeMessageSuccess() {
//      when(taskMessageDao.likeMessageByTaskMessageId(anyLong(),anyLong())).thenReturn("点赞成功！");
//      JSONObject result = taskService.likeMessage(1L, 1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testLikeMessageFailed() {
//      when(taskMessageDao.likeMessageByTaskMessageId(anyLong(),anyLong())).thenReturn("点赞失败！");
//      JSONObject result = taskService.likeMessage(1L, 1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testUnlikeMessageSuccess() {
//      when(taskMessageDao.unlikeMessageByTaskMessageId(anyLong(),anyLong())).thenReturn("取消点赞成功！");
//      JSONObject result = taskService.unlikeMessage(1L, 1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testUnlikeMessageFailed() {
//      when(taskMessageDao.unlikeMessageByTaskMessageId(anyLong(),anyLong())).thenReturn("取消点赞失败！");
//      JSONObject result = taskService.unlikeMessage(1L, 1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testCollectTaskSuccess() {
//      when(taskDao.collectTaskByTaskId(anyLong(),anyLong())).thenReturn("收藏成功！");
//      JSONObject result = taskService.collectTask(1L, 1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testCollectTaskFailed() {
//      when(taskDao.collectTaskByTaskId(anyLong(),anyLong())).thenReturn("收藏失败！");
//      JSONObject result = taskService.collectTask(1L, 1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testUncollectTaskSuccess() {
//      when(taskDao.uncollectTaskByTaskId(anyLong(),anyLong())).thenReturn("取消收藏成功！");
//      JSONObject result = taskService.uncollectTask(1L, 1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testUncollectTaskFailed() {
//      when(taskDao.uncollectTaskByTaskId(anyLong(),anyLong())).thenReturn("取消收藏失败！");
//      JSONObject result = taskService.uncollectTask(1L, 1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testAccessTaskSuccess() {
//      when(taskDao.accessTaskByTaskId(anyLong(),anyLong())).thenReturn("接取任务成功！");
//      JSONObject result = taskService.accessTask(1L, 1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testAccessTaskFailed() {
//      when(taskDao.accessTaskByTaskId(anyLong(),anyLong())).thenReturn("接取任务失败！");
//      JSONObject result = taskService.accessTask(1L, 1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testUnaccessTaskSuccess() {
//      when(taskDao.unaccessTaskByTaskId(anyLong(),anyLong())).thenReturn("取消接取任务成功！");
//      JSONObject result = taskService.unaccessTask(1L, 1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testUnaccessTaskFailed() {
//      when(taskDao.unaccessTaskByTaskId(anyLong(),anyLong())).thenReturn("取消接取任务失败！");
//      JSONObject result = taskService.unaccessTask(1L, 1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishMessageSuccess() {
//      JSONObject body = new JSONObject();
//      body.put("content", "test");
//      when(taskMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言成功！");
//      JSONObject result = taskService.publishMessage(1L, 1L, body);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testPublishMessageFailed() {
//      JSONObject body = new JSONObject();
//      body.put("content", "test");
//      when(taskMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言失败！");
//      JSONObject result = taskService.publishMessage(1L, 1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishMessageWithEmptyBody() {
//      JSONObject body = new JSONObject();
//      when(taskMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言失败！");
//      JSONObject result = taskService.publishMessage(1L, 1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishMessageWithEmptyContent() {
//      JSONObject body = new JSONObject();
//      body.put("content", "");
//      when(taskMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言失败！");
//      JSONObject result = taskService.publishMessage(1L, 1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testDeleteMessageSuccess() {
//      when(taskMessageDao.deleteMessage(anyLong(),anyLong())).thenReturn("删除留言成功！");
//      JSONObject result = taskService.deleteMessage(1L, 1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testDeleteMessageFailed() {
//      when(taskMessageDao.deleteMessage(anyLong(),anyLong())).thenReturn("删除留言失败！");
//      JSONObject result = taskService.deleteMessage(1L, 1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishCommentSuccess() {
//      JSONObject body = new JSONObject();
//      body.put("content", "test");
//      body.put("rating", 1);
//      when(taskCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论成功！");
//      JSONObject result = taskService.publishComment(1L, 1L, body);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testPublishCommentFailed() {
//      JSONObject body = new JSONObject();
//      body.put("content", "test");
//      body.put("rating", 1);
//      when(taskCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
//      JSONObject result = taskService.publishComment(1L, 1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishCommentWithEmptyBody() {
//      JSONObject body = new JSONObject();
//      when(taskCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
//      JSONObject result = taskService.publishComment(1L, 1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishCommentWithEmptyContent() {
//      JSONObject body = new JSONObject();
//      body.put("content", "");
//      body.put("rating", 1);
//      when(taskCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
//      JSONObject result = taskService.publishComment(1L, 1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishCommentWithEmptyRating() {
//      JSONObject body = new JSONObject();
//      body.put("content", "test");
//      when(taskCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
//      JSONObject result = taskService.publishComment(1L, 1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testDeleteCommentSuccess() {
//      when(taskCommentDao.deleteComment(anyLong(),anyLong())).thenReturn("删除评论成功！");
//      JSONObject result = taskService.deleteComment(1L, 1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testDeleteCommentFailed() {
//      when(taskCommentDao.deleteComment(anyLong(),anyLong())).thenReturn("删除评论失败！");
//      JSONObject result = taskService.deleteComment(1L, 1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishTaskSuccess() {
//      JSONObject body = new JSONObject();
//      body.put("title", "test");
//      body.put("description", "test");
//      body.put("price", 1000);
//      body.put("maxAccess", 1);
//      body.put("images", "test");
//      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务成功！");
//      JSONObject result = taskService.publishTask(1L, body);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testPublishTaskFailed() {
//      JSONObject body = new JSONObject();
//      body.put("title", "test");
//      body.put("description", "test");
//      body.put("price", 1000);
//      body.put("maxAccess", 1);
//      body.put("images", "test");
//      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
//      JSONObject result = taskService.publishTask(1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishTaskWithEmptyBody() {
//      JSONObject body = new JSONObject();
//      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
//      JSONObject result = taskService.publishTask(1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishTaskWithEmptyTitle() {
//      JSONObject body = new JSONObject();
//      body.put("title", "");
//      body.put("description", "test");
//      body.put("price", 1000);
//      body.put("maxAccess", 1);
//      body.put("images", "test");
//      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
//      JSONObject result = taskService.publishTask(1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishTaskWithEmptyDescription() {
//      JSONObject body = new JSONObject();
//      body.put("title", "test");
//      body.put("description", "");
//      body.put("price", 1000);
//      body.put("maxAccess", 1);
//      body.put("images", "test");
//      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
//      JSONObject result = taskService.publishTask(1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishTaskWithIntegerPrice() {
//      JSONObject body = new JSONObject();
//      body.put("title", "test");
//      body.put("description", "test");
//      body.put("price", 10000L);
//      body.put("maxAccess", 1);
//      body.put("images", "test");
//      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
//      JSONObject result = taskService.publishTask(1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishTaskWithLongPrice() {
//      JSONObject body = new JSONObject();
//      body.put("title", "test");
//      body.put("description", "test");
//      body.put("price", 10000000000L);
//      body.put("maxAccess", 1);
//      body.put("images", "test");
//      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
//      JSONObject result = taskService.publishTask(1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishTaskWithStringPrice() {
//      JSONObject body = new JSONObject();
//      body.put("title", "test");
//      body.put("description", "test");
//      body.put("price", "test");
//      body.put("maxAccess", 1);
//      body.put("images", "test");
//      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
//      JSONObject result = taskService.publishTask(1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testPublishTaskWithNegativePrice() {
//      JSONObject body = new JSONObject();
//      body.put("title", "test");
//      body.put("description", "test");
//      body.put("price", -1000);
//      body.put("maxAccess", 1);
//      body.put("images", "test");
//      when(taskDao.publishTask(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布任务失败！");
//      JSONObject result = taskService.publishTask(1L, body);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testGetTaskDtoByIdSuccess() {
//      when(taskDao.findById(anyLong())).thenReturn(task);
//      when(userClient.getUserInfo(anyLong())).thenReturn(successResponse);
//      JSONObject result = taskService.getTaskDtoById(1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testGetTaskDtoByIdFailed() {
//      when(taskDao.findById(anyLong())).thenReturn(null);
//      JSONObject result = taskService.getTaskDtoById(1L);
//      assert result.get("ok").equals(false);
//    }
//
//    @Test
//    public void testGetTaskOwnerIdSuccess() {
//      when(taskDao.findById(anyLong())).thenReturn(task);
//      JSONObject result = taskService.getTaskOwnerId(1L);
//      assert result.get("ok").equals(true);
//    }
//
//    @Test
//    public void testGetTaskOwnerIdFailed() {
//      when(taskDao.findById(anyLong())).thenReturn(null);
//      JSONObject result = taskService.getTaskOwnerId(1L);
//      assert result.get("ok").equals(false);
//    }
//}
