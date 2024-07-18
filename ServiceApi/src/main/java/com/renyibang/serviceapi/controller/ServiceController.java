package com.renyibang.serviceapi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.serviceapi.service.ServiceService;
import com.renyibang.serviceapi.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/service")
public class ServiceController {
    @Autowired ServiceService serviceService;

    @GetMapping("/search")
    public JSONObject searchService(String keyword, int pageSize, int pageIndex, String order, String timeBegin, String timeEnd, long priceLow, long priceHigh)
    {
        System.out.println("searchService");

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
            return ResponseUtil.error("排序类型错误");
        }

        if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return serviceService.searchServiceByPaging(keyword, pageable, timeBegin, timeEnd, priceLow, priceHigh);
    }

    @GetMapping("/{serviceId}")
    public JSONObject getServiceInfo(@PathVariable long serviceId)
    {
        return serviceService.getServiceInfo(serviceId);
    }

    @GetMapping("/{serviceId}/comment")
    public JSONObject getServiceComment(@PathVariable long serviceId, int pageSize, int pageIndex, String order)
    {
        Sort sort;

        if(Objects.equals(order, "likes"))
        {
            sort = Sort.by("likedNumber").descending();
        }

        else if(Objects.equals(order, "time"))
        {
            sort = Sort.by("createdAt").descending();
        }

        else
        {
            return ResponseUtil.error("排序类型错误");
        }

        if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return serviceService.getServiceComments(serviceId, pageable);
    }

    @GetMapping("/{serviceId}/message")
    public JSONObject getServiceMessage(@PathVariable long serviceId, int pageSize, int pageIndex, String order)
    {
        Sort sort;

        if(Objects.equals(order, "likes"))
        {
            sort = Sort.by("likedNumber").descending();
        }

        else if(Objects.equals(order, "time"))
        {
            sort = Sort.by("createdAt").descending();
        }

        else
        {
            return ResponseUtil.error("排序类型错误");
        }

        if (pageSize <= 0 || pageIndex < 0) return ResponseUtil.error("分页错误");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        return serviceService.getServiceMessages(serviceId, pageable);
    }

    @PutMapping("/comment/{serviceCommentId}/like")
    public JSONObject likeComment(@PathVariable long serviceCommentId, @RequestHeader long userId)
    {
        return serviceService.likeComment(serviceCommentId, userId);
    }

    @DeleteMapping("/comment/{serviceCommentId}/unlike")
    public JSONObject unlikeComment(@PathVariable long serviceCommentId, @RequestHeader long userId)
    {
        return serviceService.unlikeComment(serviceCommentId, userId);
    }

    @PutMapping("/message/{serviceMessageId}/like")
    public JSONObject likeMessage(@PathVariable long serviceMessageId, @RequestHeader long userId)
    {
        return serviceService.likeMessage(serviceMessageId, userId);
    }

    @DeleteMapping("/message/{serviceMessageId}/unlike")
    public JSONObject unlikeMessage(@PathVariable long serviceMessageId, @RequestHeader long userId)
    {
        return serviceService.unlikeMessage(serviceMessageId, userId);
    }

    @PutMapping("/{serviceId}/collect")
    public JSONObject collectService(@PathVariable long serviceId, @RequestHeader long userId)
    {
        return serviceService.collectService(serviceId, userId);
    }

    @DeleteMapping("/{serviceId}/uncollect")
    public JSONObject uncollectService(@PathVariable long serviceId, @RequestHeader long userId)
    {
        return serviceService.uncollectService(serviceId, userId);
    }

    @PutMapping("/{serviceId}/access")
    public JSONObject accessService(@PathVariable long serviceId, @RequestHeader long userId)
    {
        return serviceService.accessService(serviceId, userId);
    }

    @PutMapping("/{serviceId}/unaccess")
    public JSONObject unaccessService(@PathVariable long serviceId, @RequestHeader long userId)
    {
        return serviceService.unaccessService(serviceId, userId);
    }

    @PutMapping("/{serviceId}/message")
    public JSONObject publishMessage(@PathVariable long serviceId, @RequestBody JSONObject body, @RequestHeader long userId)
    {
        return serviceService.publishMessage(serviceId, userId, body);
    }

    @DeleteMapping("/message/{serviceMessageId}")
    public JSONObject deleteMessage(@PathVariable long serviceMessageId, @RequestHeader long userId)
    {
        return serviceService.deleteMessage(serviceMessageId, userId);
    }

    @PutMapping("/{serviceId}/comment")
    public JSONObject publishComment(@PathVariable long serviceId, @RequestBody JSONObject body, @RequestHeader long userId)
    {
        return serviceService.publishComment(serviceId, userId, body);
    }

    @DeleteMapping("/comment/{serviceCommentId}")
    public JSONObject deleteComment(@PathVariable long serviceCommentId, @RequestHeader long userId)
    {
        return serviceService.deleteComment(serviceCommentId, userId);
    }

    @PostMapping("/issue")
    public JSONObject publishService(@RequestBody JSONObject body, @RequestHeader long userId)
    {
        return serviceService.publishService(userId, body);
    }

    // 以下为后端模块API接口

    //order模块调用
    @GetMapping("/getService/{serviceId}")
    public JSONObject getServiceDtoById(@PathVariable long serviceId)
    {
        return serviceService.getServiceDtoById(serviceId);
    }

    //chat模块调用
    @GetMapping("/{serviceId}/ownerId")
    public JSONObject getServiceOwnerId(@PathVariable long serviceId)
    {
        return serviceService.getServiceOwnerId(serviceId);
    }
}