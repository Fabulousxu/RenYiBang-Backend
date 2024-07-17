package com.renyibang.serviceapi.service.serviceImpl;

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
import com.renyibang.serviceapi.service.ServiceService;
import com.renyibang.serviceapi.util.DateTimeUtil;
import com.renyibang.serviceapi.util.PriceUtil;
import com.renyibang.serviceapi.util.ResponseUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private ServiceCommentDao serviceCommentDao;

    @Autowired
    private ServiceMessageDao serviceMessageDao;

    @Autowired
    private UserClient userClient;

    private String isValidInput(LocalDateTime begin, LocalDateTime end, long low, long high)
    {
        if(begin == null || end == null)
        {
            return "时间格式错误！";
        }

        else if(begin.isAfter(end))
        {
            return "开始时间不能大于结束时间！";
        }

        else if(low < 0 || (high < 0))
        {
            return "价格不能为负数！";
        }

        else if(high >= 0 && low > high)
        {
            return "价格区间错误！";
        }

        return "OK";
    }

    @Override
    public JSONObject searchServiceByPaging(String keyword, Pageable pageable, String timeBegin, String timeEnd, long priceLow, long priceHigh)
    {
        try {
            JSONArray result = new JSONArray();
            LocalDateTime begin = DateTimeUtil.getBeginDateTime(timeBegin);
            LocalDateTime end = DateTimeUtil.getEndDateTime(timeEnd);
            long high = PriceUtil.priceConvert(priceHigh);

            String isValid = isValidInput(begin, end, priceLow, high);

            if(!Objects.equals("OK", isValid))
            {
                return ResponseUtil.error(isValid);
            }

            Page<Service> searchResult = serviceDao.searchServiceByPaging(keyword, pageable, begin, end, priceLow, high);

            //创建一个list存储用户id
            List<Long> userIds = new ArrayList<>();

            for(Service service : searchResult.getContent())
            {
                //需要传入userID
                //将userId存入list
                userIds.add(service.getOwnerId());
            }

            if(userIds.isEmpty())
            {
                JSONObject returnRes = new JSONObject();
                returnRes.put("total", searchResult.getTotalElements());
                returnRes.put("items", result);
                return ResponseUtil.success(returnRes);
            }

            JSONObject userInfos = userClient.getUserInfos(userIds);
            if(Objects.equals(false, userInfos.get("ok")))
            {
                return ResponseUtil.error("用户信息获取失败！");
            }

            ArrayList<JSONObject> userInfosArray = (ArrayList<JSONObject>) userInfos.get("data");

            for(int i = 0; i < searchResult.getContent().size(); i++)
            {
                Service service = searchResult.getContent().get(i);
                JSONObject serviceJson = service.toJSON();
                serviceJson.put("owner", userInfosArray.get(i));
                result.add(serviceJson);
            }

            JSONObject returnRes = new JSONObject();
            returnRes.put("total", searchResult.getTotalElements());
            returnRes.put("items", result);

            return ResponseUtil.success(returnRes);
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject getServiceInfo(long taskId)
    {
        try
        {
            Service result = serviceDao.findById(taskId);

            if(result == null)
            {
                return ResponseUtil.error("任务信息为null");
            }

            JSONObject serviceJson = result.toJSON();
            JSONObject response = userClient.getUserInfo(result.getOwnerId());
            if(Objects.equals(false, response.get("ok")))
            {
                return ResponseUtil.error("用户信息获取失败！");
            }

            serviceJson.put("owner", response.get("data"));

            return ResponseUtil.success(serviceJson);

        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject getServiceComments(long serviceId, Pageable pageable)
    {
        try
        {
            JSONArray result = new JSONArray();
            Page<ServiceComment> getResult = serviceCommentDao.getServiceComments(serviceId, pageable);

            List<Long> userIds = new ArrayList<>();

            for(ServiceComment serviceComment : getResult)
            {
                userIds.add(serviceComment.getCommenterId());
            }

            if(userIds.isEmpty())
            {
                JSONObject returnRes = new JSONObject();
                returnRes.put("total", getResult.getTotalElements());
                returnRes.put("items", result);
                return ResponseUtil.success(returnRes);
            }

            JSONObject userInfos = userClient.getUserInfos(userIds);
            if(Objects.equals(false, userInfos.get("ok")))
            {
                return ResponseUtil.error("用户信息获取失败！");
            }

            ArrayList<JSONObject> userInfosArray = (ArrayList<JSONObject>) userInfos.get("data");

            for(int i = 0; i < getResult.getContent().size(); i++)
            {
                ServiceComment comment = getResult.getContent().get(i);
                JSONObject serviceCommentJson = comment.toJSON();
                serviceCommentJson.put("commenter", userInfosArray.get(i));
                result.add(serviceCommentJson);
            }

            JSONObject returnRes = new JSONObject();
            returnRes.put("total", getResult.getTotalElements());
            returnRes.put("items", result);

            return ResponseUtil.success(returnRes);
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject getServiceMessages(long serviceId, Pageable pageable)
    {
        try
        {
            JSONArray result = new JSONArray();
            Page<ServiceMessage> getResult = serviceMessageDao.getServiceMessages(serviceId, pageable);

            List<Long> userIds = new ArrayList<>();

            for(ServiceMessage serviceMessage : getResult)
            {
                userIds.add(serviceMessage.getMessagerId());
            }

            if(userIds.isEmpty())
            {
                JSONObject returnRes = new JSONObject();
                returnRes.put("total", getResult.getTotalElements());
                returnRes.put("items", result);
                return ResponseUtil.success(returnRes);
            }

            JSONObject userInfos = userClient.getUserInfos(userIds);
            if(Objects.equals(false, userInfos.get("ok")))
            {
                return ResponseUtil.error("用户信息获取失败！");
            }

            ArrayList<JSONObject> userInfosArray = (ArrayList<JSONObject>) userInfos.get("data");

            for(int i = 0; i < getResult.getContent().size(); i++)
            {
                ServiceMessage serviceMessage = getResult.getContent().get(i);
                JSONObject serviceMessageJson = serviceMessage.toJSON();
                serviceMessageJson.put("messager", userInfosArray.get(i));
                result.add(serviceMessageJson);
            }

            JSONObject returnRes = new JSONObject();
            returnRes.put("total", getResult.getTotalElements());
            returnRes.put("items", result);

            return ResponseUtil.success(returnRes);
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Transactional
    @Override
    public JSONObject likeComment(long serviceCommentId, long likerId)
    {
        try
        {
            String result = serviceCommentDao.likeCommentByServiceCommentId(serviceCommentId, likerId);
            if("点赞成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Transactional
    @Override
    public JSONObject unlikeComment(long serviceCommentId, long unlikerId)
    {
        try
        {
            String result = serviceCommentDao.unlikeCommentByServiceCommentId(serviceCommentId, unlikerId);
            if("取消点赞成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Transactional
    @Override
    public JSONObject likeMessage(long serviceMessageId, long likerId)
    {
        try
        {
            String result = serviceMessageDao.likeMessageByServiceMessageId(serviceMessageId, likerId);
            if("点赞成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Transactional
    @Override
    public JSONObject unlikeMessage(long serviceMessageId, long unlikerId)
    {
        try
        {
            String result = serviceMessageDao.unlikeMessageByServiceMessageId(serviceMessageId, unlikerId);
            if("取消点赞成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Transactional
    @Override
    public JSONObject collectService(long ServiceId, long collectorId)
    {
        try
        {
            String result = serviceDao.collectServiceByServiceId(ServiceId, collectorId);
            if("收藏成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Transactional
    @Override
    public JSONObject uncollectService(long ServiceId, long uncollectorId)
    {
        try
        {
            String result = serviceDao.uncollectServiceByServiceId(ServiceId, uncollectorId);
            if("取消收藏成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Transactional
    @Override
    public JSONObject accessService(long ServiceId, long accessorId)
    {
        try
        {
            String result = serviceDao.accessServiceByServiceId(ServiceId, accessorId);
            if("接取服务成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Transactional
    @Override
    public JSONObject unaccessService(long ServiceId, long unaccessorId)
    {
        try
        {
            String result = serviceDao.unaccessServiceByServiceId(ServiceId, unaccessorId);
            if("取消接取服务成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Override
    public JSONObject publishMessage(long serviceId, long userId, JSONObject body)
    {
        try
        {
            Object requestContent = body.get("content");
            if(requestContent == null)
            {
                return ResponseUtil.error("请求体为空！");
            }

            String content = requestContent.toString();
            if(content.isEmpty())
            {
                return ResponseUtil.error("留言内容为空！");
            }
            String result = serviceMessageDao.putMessage(serviceId, userId, content);
            if("发布留言成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Override
    public JSONObject deleteMessage(long serviceMessageId, long userId)
    {
        try
        {
            String result = serviceMessageDao.deleteMessage(serviceMessageId, userId);
            if("删除留言成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Override
    public JSONObject publishComment(long serviceId, long userId, JSONObject body)
    {
        try
        {
            Object requestContent = body.get("content");
            Object requestRating = body.get("rating");
            if(requestContent == null || requestRating == null)
            {
                return ResponseUtil.error("请求体不完整！");
            }

            String content = requestContent.toString();
            if(content.isEmpty())
            {
                return ResponseUtil.error("评论内容为空！");
            }

            byte rating = body.getByteValue("rating");

            String result = serviceCommentDao.putComment(serviceId, userId, content, rating);
            if("发布评论成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Override
    public JSONObject deleteComment(long serviceCommentId, long userId)
    {
        try
        {
            String result = serviceCommentDao.deleteComment(serviceCommentId, userId);
            if("删除评论成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }

            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Override
    public JSONObject publishService(long userId, JSONObject body)
    {
        try
        {
            Object requestTitle = body.get("title");
            Object requestDescription = body.get("description");
            Object requestPrice = body.get("price");
            Object requestImages = body.get("images");

            if(requestTitle == null || requestDescription == null || requestPrice == null || requestImages == null)
            {
                return ResponseUtil.error("请求体不完整！");
            }

            String title = requestTitle.toString();
            if(title.isEmpty())
            {
                return ResponseUtil.error("任务标题为空！");
            }

            String description = requestDescription.toString();
            if(description.isEmpty())
            {
                return ResponseUtil.error("任务内容为空！");
            }

            long price = 0L;

            if(requestPrice.getClass() == Integer.class)
            {
                price = body.getInteger("price").longValue();
            }

            else if(requestPrice.getClass() == Long.class)
            {
                price = body.getLongValue("price");
            }

            else
            {
                return ResponseUtil.error("非法的价格类型！");
            }

            if(price < 0)
            {
                return ResponseUtil.error("价格不能为负数！");
            }

            String result = serviceDao.publishService(userId, title, description, price, (List<String>)requestImages);

            if("服务发布成功！".equals(result))
            {
                return ResponseUtil.success(result);
            }
            else
            {
                return ResponseUtil.error(result);
            }
        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e.getMessage()));
        }
    }

    @Override
    public JSONObject getServiceDtoById(Long serviceId)
    {
        Service service = serviceDao.findById(serviceId);
        if(service == null)
        {
            return ResponseUtil.error("服务不存在！");
        }

        return ResponseUtil.success(new ServiceDTO(service.getServiceId(), service.getTitle(), service.getDescription(), service.getImages(), service.getCreatedAt()));
    }

    @Override
    public JSONObject getServiceOwnerId(long serviceId)
    {
        Service service = serviceDao.findById(serviceId);
        if(service == null)
        {
            return ResponseUtil.error("服务不存在！");
        }

        return ResponseUtil.success(service.getOwnerId());
    }
}