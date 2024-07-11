package com.renyibang.taskapi.service.serviceImpl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.client.UserClient;
import com.renyibang.global.dto.TaskDTO;
import com.renyibang.taskapi.dao.TaskCommentDao;
import com.renyibang.taskapi.dao.TaskDao;
import com.renyibang.taskapi.dao.TaskMessageDao;
import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskComment;
import com.renyibang.taskapi.entity.TaskMessage;
import com.renyibang.taskapi.service.TaskService;
import com.renyibang.taskapi.util.DateTimeUtil;
import com.renyibang.taskapi.util.PriceUtil;
import com.renyibang.taskapi.util.ResponseUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskDao taskDao;

    @Autowired
    TaskCommentDao taskCommentDao;

    @Autowired
    TaskMessageDao taskMessageDao;

    @Autowired
    UserClient userClient;

    @Override
    public JSONObject searchTaskByPaging(String keyword, Pageable pageable, String timeBegin, String timeEnd, long priceLow, long priceHigh)
    {
        try{
            JSONArray result = new JSONArray();
            Page<Task> searchResult = taskDao.searchTaskByPaging(keyword, pageable, DateTimeUtil.getBeginDateTime(timeBegin), DateTimeUtil.getEndDateTime(timeEnd), priceLow, PriceUtil.priceConvert(priceHigh));

            //创建一个list存储用户id
            List<Long> userIds = new ArrayList<>();

            for(Task task : searchResult.getContent())
            {
                //需要传入userId
                //将userId存入list
                userIds.add(task.getOwnerId());
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
            //将用户信息存入taskJson
            for(int i = 0; i < searchResult.getContent().size(); i++)
            {
                Task task = searchResult.getContent().get(i);
                JSONObject taskJson = task.toJSON();
                taskJson.put("owner", userInfosArray.get(i));
                result.add(taskJson);
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
    public JSONObject getTaskInfo(long taskId)
    {
        try
        {
            Task result = taskDao.findById(taskId);

            if(result == null)
            {
                return ResponseUtil.error("任务信息为null");
            }

            JSONObject taskJson = result.toJSON();
            JSONObject response = userClient.getUserInfo(result.getOwnerId());
            if(Objects.equals(false, response.get("ok")))
            {
                return ResponseUtil.error("用户信息获取失败！");
            }

            taskJson.put("owner", response.get("data"));

            return ResponseUtil.success(taskJson);

        }
        catch (Exception e)
        {
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject getTaskComments(long taskId, Pageable pageable)
    {
        try
        {
            JSONArray result = new JSONArray();
            Page<TaskComment> getResult = taskCommentDao.getTaskComments(taskId, pageable);

            List<Long> userIds = new ArrayList<>();

            for(TaskComment taskComment : getResult)
            {
                userIds.add(taskComment.getCommenterId());
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
                TaskComment taskComment = getResult.getContent().get(i);
                JSONObject taskCommentJson = taskComment.toJSON();
                taskCommentJson.put("commenter", userInfosArray.get(i));
                result.add(taskCommentJson);
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
    public JSONObject getTaskMessages(long taskId, Pageable pageable)
    {
        try
        {
            JSONArray result = new JSONArray();
            Page<TaskMessage> getResult = taskMessageDao.getTaskMessages(taskId, pageable);

            List<Long> userIds = new ArrayList<>();

            for(TaskMessage taskMessage : getResult)
            {
                userIds.add(taskMessage.getMessagerId());
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
                TaskMessage taskMessage = getResult.getContent().get(i);
                JSONObject taskMessageJson = taskMessage.toJSON();
                taskMessageJson.put("messager", userInfosArray.get(i));
                result.add(taskMessageJson);
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
    public JSONObject likeComment(long taskCommentId, long likerId)
    {
        try
        {
            String result = taskCommentDao.likeCommentByTaskCommentId(taskCommentId, likerId);
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
    public JSONObject unlikeComment(long taskCommentId, long unlikerId)
    {
        try
        {
            String result = taskCommentDao.unlikeCommentByTaskCommentId(taskCommentId, unlikerId);
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
    public JSONObject likeMessage(long taskMessageId, long likerId)
    {
        try
        {
            String result = taskMessageDao.likeMessageByTaskMessageId(taskMessageId, likerId);
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
    public JSONObject unlikeMessage(long taskMessageId, long unlikerId)
    {
        try
        {
            String result = taskMessageDao.unlikeMessageByTaskMessageId(taskMessageId, unlikerId);
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
    public JSONObject collectTask(long taskId, long collectorId)
    {
        try
        {
            String result = taskDao.collectTaskByTaskId(taskId, collectorId);
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
    public JSONObject uncollectTask(long taskId, long uncollectorId)
    {
        try
        {
            String result = taskDao.uncollectTaskByTaskId(taskId, uncollectorId);
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
    public JSONObject accessTask(long taskId, long accessorId)
    {
        try
        {
            String result = taskDao.accessTaskByTaskId(taskId, accessorId);
            if("接取任务成功！".equals(result))
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
    public JSONObject unaccessTask(long taskId, long unaccessorId)
    {
        try
        {
            String result = taskDao.unaccessTaskByTaskId(taskId, unaccessorId);
            if("取消接取任务成功！".equals(result))
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
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject publishMessage(long taskId, long userId, JSONObject body)
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
            String result = taskMessageDao.putMessage(taskId, userId, content);
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
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject deleteMessage(long taskMessageId, long userId)
    {
        try
        {
            String result = taskMessageDao.deleteMessage(taskMessageId, userId);
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
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject publishComment(long taskId, long userId, JSONObject body)
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

            byte rating = body.getByteValue("rating");;

            String result = taskCommentDao.putComment(taskId, userId, content, rating);
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
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject deleteComment(long taskCommentId, long userId)
    {
        try
        {
            String result = taskCommentDao.deleteComment(taskCommentId, userId);
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
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject publishTask(long userId, JSONObject body)
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

            String result = taskDao.publishTask(userId, title, description, price, (List<String>)requestImages);
            if("任务发布成功！".equals(result))
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
            return ResponseUtil.error(String.valueOf(e));
        }
    }

    @Override
    public JSONObject getTaskDtoById(Long taskId)
    {
        Task task = taskDao.findById(taskId);
        if(task == null)
        {
            return ResponseUtil.error("任务不存在！");
        }

        return ResponseUtil.success(new TaskDTO(task.getTaskId(), task.getTitle(), task.getDescription(), task.getImages(), task.getCreatedAt()));
    }

    @Override
    public JSONObject getTaskOwnerId(long taskId)
    {
        Task task = taskDao.findById(taskId);
        if(task == null)
        {
            return ResponseUtil.error("任务不存在！");
        }

        return ResponseUtil.success(task.getOwnerId());
    }
}
