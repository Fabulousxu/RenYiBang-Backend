package com.renyibang.serviceapi.service.serviceImpl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.renyibang.moduleapi.clients.UserClient;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceDao serviceDao;

    @Autowired
    private ServiceCommentDao serviceCommentDao;

    @Autowired
    private ServiceMessageDao serviceMessageDao;

    @Autowired
    private UserClient userClient;

    @Override
    public JSONObject searchServiceByPaging(String keyword, Pageable pageable, String timeBegin, String timeEnd, long priceLow, long priceHigh)
    {
        try {
            JSONArray result = new JSONArray();
            Page<Service> searchResult = serviceDao.searchServiceByPaging(keyword, pageable, DateTimeUtil.getBeginDateTime(timeBegin), DateTimeUtil.getEndDateTime(timeEnd), priceLow, priceConvert(priceHigh));

            for(Service service : searchResult.getContent())
            {
                //需要传入userID
                JSONObject serviceJson = service.toJSON();

                taskJson.put("owner", userClient.getUserJSON(task.getOwner_id()));

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
            JSONObject serviceJson = result.toJSON();
            serviceJson.put("owner", userClient.getUserJSON(result.getOwner_id()));

            if(result == null)
            {
                return ResponseUtil.error("任务信息为null");
            }

            else
            {
                return ResponseUtil.success(taskJson);
            }
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
            Page<ServiceComment> getResult = ServiceCommentDao.getServiceComments(serviceId, pageable);

            for(ServiceComment comment : getResult)
            {
                JSONObject serviceCommentJson = comment.toJSON();
                serviceCommentJson.put("commenter", userClient.getUserJSON(comment.getCommenter_id()));

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
            Page<ServiceMessage> getResult = ServiceMessageDao.getServiceMessages(serviceId, pageable);

            for(ServiceMessage message : getResult)
            {
                JSONObject serviceMessageJson = message.toJSON();
                serviceMessageJson.put("messager", userClient.getUserJSON(message.getMessager_id()));

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
            String result = ServiceCommentDao.likeCommentByServiceCommentId(serviceCommentId, likerId);
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
            String result = ServiceCommentDao.unlikeCommentByServiceCommentId(serviceCommentId, unlikerId);
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
            String result = ServiceMessageDao.likeMessageByServiceMessageId(serviceMessageId, likerId);
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
            String result = ServiceMessageDao.unlikeMessageByServiceMessageId(serviceMessageId, unlikerId);
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
            String result = ServiceDao.collectServiceByServiceId(ServiceId, collectorId);
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
            String result = ServiceDao.uncollectServiceByServiceId(ServiceId, uncollectorId);
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
            String result = ServiceDao.accessServiceByServiceId(ServiceId, accessorId);
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
            String result = ServiceDao.unaccessServiceByServiceId(ServiceId, unaccessorId);
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
            String result = ServiceMessageDao.publishMessage(serviceId, userId, content);
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
            String result = ServiceMessageDao.deleteMessage(serviceMessageId, userId);
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

            String result = ServiceCommentDao.publishComment(serviceId, userId, content, rating);
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
            String result = ServiceCommentDao.deleteComment(serviceCommentId, userId);
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

            String result = ServiceDao.publishService(userId, title, description, price, (List<String>)requestImages);

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
}