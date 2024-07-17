package com.renyibang.serviceapi.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.client.UserClient;
import com.renyibang.serviceapi.dao.ServiceCommentDao;
import com.renyibang.serviceapi.dao.ServiceDao;
import com.renyibang.serviceapi.dao.ServiceMessageDao;
import com.renyibang.serviceapi.entity.Service;
import com.renyibang.serviceapi.entity.ServiceComment;
import com.renyibang.serviceapi.entity.ServiceMessage;
import com.renyibang.serviceapi.service.serviceImpl.ServiceServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ServiceServiceImplTest {
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
    private ServiceServiceImpl serviceService;

    private Service service;
    private Pageable pageable;
    private DateTimeFormatter formatter;
    private List<Service> servicelist;
    private Page<Service> servicepage;
    private LocalDateTime begintime;
    private LocalDateTime endtime;
    private JSONObject successResponse;
    private JSONObject errorResponse;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(serviceService).build();
        service = new Service();
        service.setOwnerId(1L);
        service.setTitle("test");
        service.setDescription("test");
        service.setPrice(1000);
        service.setCreatedAt(LocalDateTime.parse("2024-06-01 00:00:00", formatter));
        servicelist.add(service);
        servicepage = new PageImpl<>(servicelist);
        pageable = PageRequest.of(0, 10);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        begintime = LocalDateTime.parse("2024-06-01 00:00:00", formatter);
        endtime = LocalDateTime.parse("2024-06-02 00:00:00", formatter);
        successResponse.put("ok", true);
        errorResponse.put("ok", false);
    }

    @Test
    public void testSearchServiceByPaging() {
        LocalDateTime begintime = LocalDateTime.parse("2024-06-01 00:00:00", formatter);
        LocalDateTime endtime = LocalDateTime.parse("2024-06-02 00:00:00", formatter);
        JSONArray userIds = new JSONArray();
        userIds.add(1L);
        JSONObject userObject = new JSONObject();
        userObject.put("ok", true);
        userObject.put("data", userIds);

        when(serviceDao.searchServiceByPaging(anyString(),any(Pageable.class),any(LocalDateTime.class),any(LocalDateTime.class),anyLong(),anyLong())).thenReturn(servicepage);
        when(userClient.getUserInfos(anyList())).thenReturn(userObject);

        //执行测试
        JSONObject page = serviceService.searchServiceByPaging("test", pageable, begintime.toString(), endtime.toString(), 1, 10000);
        assert page.get("ok").equals(true);
    }

    @Test
    public void testSearchServiceByPagingWithInvalidParams() {
        JSONObject page = serviceService.searchServiceByPaging("test", pageable, begintime.toString(), endtime.toString(), -1, -2);
        assert page.get("ok").equals(false);
    }

    @Test
    public void testSearchServiceByPagingWithEmptyUserIds() {
        JSONArray userIds = new JSONArray();
        JSONObject userObject = new JSONObject();
        userObject.put("ok", false);
        userObject.put("data", userIds);

        when(serviceDao.searchServiceByPaging(anyString(),any(Pageable.class),any(LocalDateTime.class),any(LocalDateTime.class),anyLong(),anyLong())).thenReturn(servicepage);
        when(userClient.getUserInfos(anyList())).thenReturn(userObject);

        JSONObject page = serviceService.searchServiceByPaging("test", pageable, begintime.toString(), endtime.toString(), 1, 10000);
        assert page.get("ok").equals(false);
    }

    @Test
    public void testGetServiceInfo() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        when(userClient.getUserInfo(anyLong())).thenReturn(successResponse);
        JSONObject result = serviceService.getServiceInfo(1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceInfoWithEmptyService() {
        when(serviceDao.findById(anyLong())).thenReturn(null);
        JSONObject result = serviceService.getServiceInfo(1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceInfoWithEmptyUser() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        when(userClient.getUserInfo(anyLong())).thenReturn(errorResponse);
        JSONObject result = serviceService.getServiceInfo(1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceComments() {
        ServiceComment serviceComment = new ServiceComment();
        serviceComment.setCommenterId(1L);
        Page<ServiceComment> serviceCommentPage = new PageImpl<>(List.of(serviceComment));
        when(serviceCommentDao.getServiceComments(anyLong(),any(Pageable.class))).thenReturn(serviceCommentPage);
        when(userClient.getUserInfos(anyList())).thenReturn(successResponse);

        JSONObject result = serviceService.getServiceComments(1, pageable);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceCommentsWithEmptyComments() {
        Page<ServiceComment> serviceCommentPage = new PageImpl<>(new ArrayList<>());
        when(serviceCommentDao.getServiceComments(anyLong(),any(Pageable.class))).thenReturn(serviceCommentPage);
        JSONObject result = serviceService.getServiceComments(1, pageable);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceCommentsWithEmptyUser() {
        ServiceComment serviceComment = new ServiceComment();
        serviceComment.setCommenterId(1L);
        Page<ServiceComment> serviceCommentPage = new PageImpl<>(List.of(serviceComment));
        when(serviceCommentDao.getServiceComments(anyLong(),any(Pageable.class))).thenReturn(serviceCommentPage);
        when(userClient.getUserInfos(anyList())).thenReturn(errorResponse);

        JSONObject result = serviceService.getServiceComments(1, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceMessages() {
        ServiceMessage serviceMessage = new ServiceMessage();
        serviceMessage.setMessagerId(1L);
        Page<ServiceMessage> serviceMessagePage = new PageImpl<>(List.of(serviceMessage));
        when(serviceMessageDao.getServiceMessages(anyLong(),any(Pageable.class))).thenReturn(serviceMessagePage);
        when(userClient.getUserInfos(anyList())).thenReturn(successResponse);

        JSONObject result = serviceService.getServiceMessages(1, pageable);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceMessagesWithEmptyMessages() {
        Page<ServiceMessage> serviceMessagePage = new PageImpl<>(new ArrayList<>());
        when(serviceMessageDao.getServiceMessages(anyLong(),any(Pageable.class))).thenReturn(serviceMessagePage);
        JSONObject result = serviceService.getServiceMessages(1, pageable);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceMessagesWithEmptyUser() {
        ServiceMessage serviceMessage = new ServiceMessage();
        serviceMessage.setMessagerId(1L);
        Page<ServiceMessage> serviceMessagePage = new PageImpl<>(List.of(serviceMessage));
        when(serviceMessageDao.getServiceMessages(anyLong(),any(Pageable.class))).thenReturn(serviceMessagePage);
        when(userClient.getUserInfos(anyList())).thenReturn(errorResponse);

        JSONObject result = serviceService.getServiceMessages(1, pageable);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testLikeCommentSuccess() {
        when(serviceCommentDao.likeCommentByServiceCommentId(anyLong(),anyLong())).thenReturn("点赞成功！");
        JSONObject result = serviceService.likeComment(1, 1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testLikeCommentFailed() {
        when(serviceCommentDao.likeCommentByServiceCommentId(anyLong(),anyLong())).thenReturn("点赞失败！");
        JSONObject result = serviceService.likeComment(1, 1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testUnlikeCommentSuccess() {
        when(serviceCommentDao.unlikeCommentByServiceCommentId(anyLong(),anyLong())).thenReturn("取消点赞成功！");
        JSONObject result = serviceService.unlikeComment(1, 1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testUnlikeCommentFailed() {
        when(serviceCommentDao.unlikeCommentByServiceCommentId(anyLong(),anyLong())).thenReturn("取消点赞失败！");
        JSONObject result = serviceService.unlikeComment(1, 1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testLikeMessageSuccess() {
        when(serviceMessageDao.likeMessageByServiceMessageId(anyLong(),anyLong())).thenReturn("点赞成功！");
        JSONObject result = serviceService.likeMessage(1, 1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testLikeMessageFailed() {
        when(serviceMessageDao.likeMessageByServiceMessageId(anyLong(),anyLong())).thenReturn("点赞失败！");
        JSONObject result = serviceService.likeMessage(1, 1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testUnlikeMessageSuccess() {
        when(serviceMessageDao.unlikeMessageByServiceMessageId(anyLong(),anyLong())).thenReturn("取消点赞成功！");
        JSONObject result = serviceService.unlikeMessage(1, 1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testUnlikeMessageFailed() {
        when(serviceMessageDao.unlikeMessageByServiceMessageId(anyLong(),anyLong())).thenReturn("取消点赞失败！");
        JSONObject result = serviceService.unlikeMessage(1, 1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testCollectServiceSuccess() {
        when(serviceDao.collectServiceByServiceId(anyLong(),anyLong())).thenReturn("收藏成功！");
        JSONObject result = serviceService.collectService(1, 1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testCollectServiceFailed() {
        when(serviceDao.collectServiceByServiceId(anyLong(),anyLong())).thenReturn("收藏失败！");
        JSONObject result = serviceService.collectService(1, 1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testUncollectServiceSuccess() {
        when(serviceDao.uncollectServiceByServiceId(anyLong(),anyLong())).thenReturn("取消收藏成功！");
        JSONObject result = serviceService.uncollectService(1, 1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testUncollectServiceFailed() {
        when(serviceDao.uncollectServiceByServiceId(anyLong(),anyLong())).thenReturn("取消收藏失败！");
        JSONObject result = serviceService.uncollectService(1, 1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testAccessServiceSuccess() {
        when(serviceDao.accessServiceByServiceId(anyLong(),anyLong())).thenReturn("接取服务成功！");
        JSONObject result = serviceService.accessService(1, 1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testAccessServiceFailed() {
        when(serviceDao.accessServiceByServiceId(anyLong(),anyLong())).thenReturn("接取服务失败！");
        JSONObject result = serviceService.accessService(1, 1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testUnaccessServiceSuccess() {
        when(serviceDao.unaccessServiceByServiceId(anyLong(),anyLong())).thenReturn("取消接取服务成功！");
        JSONObject result = serviceService.unaccessService(1, 1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testUnaccessServiceFailed() {
        when(serviceDao.unaccessServiceByServiceId(anyLong(),anyLong())).thenReturn("取消接取服务失败！");
        JSONObject result = serviceService.unaccessService(1, 1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishMessageSuccess() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        when(serviceMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言成功！");
        JSONObject result = serviceService.publishMessage(1, 1, body);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testPublishMessageFailed() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        when(serviceMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言失败！");
        JSONObject result = serviceService.publishMessage(1, 1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishMessageWithEmptyBody() {
        JSONObject body = new JSONObject();
        when(serviceMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言失败！");
        JSONObject result = serviceService.publishMessage(1, 1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishMessageWithEmptyContent() {
        JSONObject body = new JSONObject();
        body.put("content", "");
        when(serviceMessageDao.putMessage(anyLong(),anyLong(),anyString())).thenReturn("发布留言失败！");
        JSONObject result = serviceService.publishMessage(1, 1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testDeleteMessageSuccess() {
        when(serviceMessageDao.deleteMessage(anyLong(),anyLong())).thenReturn("删除留言成功！");
        JSONObject result = serviceService.deleteMessage(1, 1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testDeleteMessageFailed() {
        when(serviceMessageDao.deleteMessage(anyLong(),anyLong())).thenReturn("删除留言失败！");
        JSONObject result = serviceService.deleteMessage(1, 1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentSuccess() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        body.put("rating", 1);
        when(serviceCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论成功！");
        JSONObject result = serviceService.publishComment(1, 1, body);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testPublishCommentFailed() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        body.put("rating", 1);
        when(serviceCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
        JSONObject result = serviceService.publishComment(1, 1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentWithEmptyBody() {
        JSONObject body = new JSONObject();
        when(serviceCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
        JSONObject result = serviceService.publishComment(1, 1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentWithEmptyContent() {
        JSONObject body = new JSONObject();
        body.put("content", "");
        body.put("rating", 1);
        when(serviceCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
        JSONObject result = serviceService.publishComment(1, 1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishCommentWithEmptyRating() {
        JSONObject body = new JSONObject();
        body.put("content", "test");
        when(serviceCommentDao.putComment(anyLong(),anyLong(),anyString(),anyByte())).thenReturn("发布评论失败！");
        JSONObject result = serviceService.publishComment(1, 1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testDeleteCommentSuccess() {
        when(serviceCommentDao.deleteComment(anyLong(),anyLong())).thenReturn("删除评论成功！");
        JSONObject result = serviceService.deleteComment(1, 1);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testDeleteCommentFailed() {
        when(serviceCommentDao.deleteComment(anyLong(),anyLong())).thenReturn("删除评论失败！");
        JSONObject result = serviceService.deleteComment(1, 1);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceSuccess() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 1000);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务成功！");
        JSONObject result = serviceService.publishService(1, body);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testPublishServiceFailed() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 1000);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithEmptyBody() {
        JSONObject body = new JSONObject();
        when(serviceDao.publishService(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithEmptyTitle() {
        JSONObject body = new JSONObject();
        body.put("title", "");
        body.put("description", "test");
        body.put("price", 1000);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithEmptyDescription() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "");
        body.put("price", 1000);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithIntegerPrice() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 0);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithLongPrice() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", 10000000000000L);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithStringPrice() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", "test");
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testPublishServiceWithNegativePrice() {
        JSONObject body = new JSONObject();
        body.put("title", "test");
        body.put("description", "test");
        body.put("price", -1);
        body.put("maxAccess", 1);
        body.put("images", "test");
        when(serviceDao.publishService(anyLong(),anyString(),anyString(),anyLong(), ArgumentMatchers.<List<String>>any())).thenReturn("发布服务失败！");
        JSONObject result = serviceService.publishService(1, body);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceDtoByIdSuccess() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        JSONObject result = serviceService.getServiceDtoById(1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceDtoByIdFailed() {
        when(serviceDao.findById(anyLong())).thenReturn(null);
        JSONObject result = serviceService.getServiceDtoById(1L);
        assert result.get("ok").equals(false);
    }

    @Test
    public void testGetServiceOwnerIdSuccess() {
        when(serviceDao.findById(anyLong())).thenReturn(service);
        JSONObject result = serviceService.getServiceOwnerId(1L);
        assert result.get("ok").equals(true);
    }

    @Test
    public void testGetServiceOwnerIdFailed() {
        when(serviceDao.findById(anyLong())).thenReturn(null);
        JSONObject result = serviceService.getServiceOwnerId(1L);
        assert result.get("ok").equals(false);
    }
}
