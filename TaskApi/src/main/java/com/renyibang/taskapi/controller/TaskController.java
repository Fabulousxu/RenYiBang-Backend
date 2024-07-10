package com.renyibang.taskapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.dto.TaskDTO;
import com.renyibang.taskapi.service.TaskService;
import com.renyibang.taskapi.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/task")
@CrossOrigin
public class TaskController {
    @Autowired TaskService taskService;

    @GetMapping("/search")
    public JSONObject searchTask(String keyword, int pageSize, int pageIndex, String order, String timeBegin, String timeEnd, long priceLow, long priceHigh)
    {
        Sort sort;

        if(Objects.equals(order, "time"))
        {
            sort = Sort.by("createdAt").descending();
        }

        else if (Objects.equals(order, "rating"))
        {
            sort = Sort.by("rating").descending();
        }

        else
        {
            return ResponseUtil.error("Undefined order");
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return taskService.searchTaskByPaging(keyword, pageable, timeBegin, timeEnd, priceLow, priceHigh);
    }

    @GetMapping("/{taskId}")
    public JSONObject getTaskInfo(@PathVariable long taskId)
    {
        return taskService.getTaskInfo(taskId);
    }

    @GetMapping("/{taskId}/comment")
    public JSONObject getTaskComment(@PathVariable long taskId, int pageSize, int pageIndex, String order)
    {
        Sort sort;

        if(Objects.equals(order, "likes"))
        {
            sort = Sort.by("likedNumber").descending();
        }

        else if (Objects.equals(order, "time"))
        {
            sort = Sort.by("createdAt").descending();
        }

        else
        {
            return ResponseUtil.error("Undefined order");
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return taskService.getTaskComments(taskId, pageable);
    }

    @GetMapping("/{taskId}/message")
    public JSONObject getTaskMessage(@PathVariable long taskId, int pageSize, int pageIndex, String order)
    {
        Sort sort;

        if(Objects.equals(order, "likes"))
        {
            sort = Sort.by("likedNumber").descending();
        }

        else if (Objects.equals(order, "time"))
        {
            sort = Sort.by("createdAt").descending();
        }

        else
        {
            return ResponseUtil.error("Undefined order");
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return taskService.getTaskMessages(taskId, pageable);
    }

    @PutMapping("/comment/{taskCommentId}/like")
    public JSONObject likeComment(@PathVariable long taskCommentId)
    {
        //userId待替换
        long likerId = 1;

        return taskService.likeComment(taskCommentId, likerId);
    }

    @DeleteMapping("/comment/{taskCommentId}/unlike")
    public JSONObject unlikeComment(@PathVariable long taskCommentId)
    {
        //userId待替换
        long unlikerId = 1;

        return taskService.unlikeComment(taskCommentId, unlikerId);
    }

    @PutMapping("/message/{taskMessageId}/like")
    public JSONObject likeMessage(@PathVariable long taskMessageId)
    {
        //userId待替换
        long likerId = 1;
        return taskService.likeMessage(taskMessageId, likerId);
    }

    @DeleteMapping("/message/{taskMessageId}/unlike")
    public JSONObject unlikeMessage(@PathVariable long taskMessageId)
    {
        //userId待替换
        long unlikerId = 1;
        return taskService.unlikeMessage(taskMessageId, unlikerId);
    }

    @PutMapping("/{taskId}/collect")
    public JSONObject collectTask(@PathVariable long taskId)
    {
        //userId待替换
        long collectorId = 1;
        return taskService.collectTask(taskId, collectorId);
    }

    @DeleteMapping("/{taskId}/uncollect")
    public JSONObject uncollectTask(@PathVariable long taskId)
    {
        //userId待替换
        long uncollectorId = 1;
        return taskService.uncollectTask(taskId, uncollectorId);
    }

    @PutMapping("/{taskId}/access")
    public JSONObject accessTask(@PathVariable long taskId, long userId)
    {
        //userId待替换
        long accessorId = 1;
        return taskService.accessTask(taskId, userId);
    }

    @DeleteMapping("/{taskId}/unaccess")
    public JSONObject unaccessTask(@PathVariable long taskId, long userId)
    {
        //userId待替换
        long unaccessorId = 1;
        return taskService.unaccessTask(taskId, userId);
    }

    @PutMapping("/{taskId}/message")
    public JSONObject publishMessage(@PathVariable long taskId, @RequestBody JSONObject body, long userId)
    {
        //userId待替换
        long publisherId = 1;
        return taskService.publishMessage(taskId, userId, body);
    }

    @DeleteMapping("/message/{taskMessageId}")
    public JSONObject deleteMessage(@PathVariable long taskMessageId, long userId)
    {
        //userId待替换
        long messagerId = 1;
        return taskService.deleteMessage(taskMessageId, userId);
    }

    @PutMapping("/{taskId}/comment")
    public JSONObject publishComment(@PathVariable long taskId, @RequestBody JSONObject body, long userId)
    {
        //userId待替换
        long commenterId = 1;
        return taskService.publishComment(taskId, userId, body);
    }

    @DeleteMapping("/comment/{taskCommentId}")
    public JSONObject deleteComment(@PathVariable long taskCommentId, long userId)
    {
        //userId待替换
        long commenterId = 1;
        return taskService.deleteComment(taskCommentId, userId);
    }

    @PostMapping("/issue")
    public JSONObject publishTask(@RequestBody JSONObject body, long userId)
    {
        //userId待替换
        long publisherId = 1;
        return taskService.publishTask(userId, body);
    }

    // 以下为后端模块API接口

    //order模块调用
    @GetMapping("/getTask/{taskId}")
    public TaskDTO getTaskById(@PathVariable Long taskId)
    {
        return taskService.getTaskById(taskId);
    }


    //chat模块调用
    @GetMapping("/{taskId}/ownerId")
    public JSONObject getTaskOwnerId(@PathVariable long taskId)
    {
        return taskService.getTaskOwnerId(taskId);
    }
}
