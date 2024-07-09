package com.renyibang.taskapi.dao.daoImpl;

import com.renyibang.moduleapi.clients.UserClient;
import com.renyibang.taskapi.dao.TaskDao;
import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskAccess;
import com.renyibang.taskapi.entity.TaskCollect;
import com.renyibang.taskapi.enums.TaskStatus;
import com.renyibang.taskapi.repository.TaskAccessRepository;
import com.renyibang.taskapi.repository.TaskCollectRepository;
import com.renyibang.taskapi.repository.TaskRepository;
import com.renyibang.taskapi.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TaskDaoImpl implements TaskDao {
  @Autowired TaskRepository taskRepository;

  @Autowired TaskCollectRepository taskCollectRepository;

  @Autowired TaskAccessRepository taskAccessRepository;

  @Autowired UserClient userClient;


  @Override
  public Task findById(long taskId) {
    return taskRepository.findById(taskId).orElse(null);
  }

  @Override
  public Page<Task> searchTaskByPaging(String keyword, Pageable pageable, LocalDateTime beginDateTime, LocalDateTime endDateTime, long priceLow, long priceHigh)
  {
      if(!keyword.isEmpty())
      {
          return taskRepository.searchTasks(keyword, priceLow, priceHigh, beginDateTime, endDateTime, TaskStatus.DELETE, pageable);
      }
      else
      {
          return taskRepository.findByPriceBetweenAndCreatedAtBetweenAndStatusNot(priceLow, priceHigh, beginDateTime, endDateTime, TaskStatus.DELETE, pageable);
      }
  }

  @Override
  public String collectTaskByTaskId(long taskId, long collectorId)
  {
      try{
          if(!userClient.getUserExist(collectorId))
          {
              return "用户不存在！";
          }

          Task task = taskRepository.findById(taskId).orElse(null);
          if(task == null)
          {
              return "任务不存在！";
          }

          if(task.getStatus() == TaskStatus.DELETE)
          {
              return "该任务已被删除！";
          }

          if(taskCollectRepository.existsByCollectorIdAndAndTask(collectorId, task))
          {
              return "用户已收藏该任务！";
          }

          task.setCollectedNumber(task.getCollectedNumber() + 1);
          TaskCollect taskCollect = new TaskCollect();
          taskCollect.setTask(task);
          taskCollect.setCollectorId(collectorId);
          taskCollect.setCreatedAt(LocalDateTime.now());

          taskCollectRepository.save(taskCollect);

          return "收藏成功！";
      }
      catch (Exception e)
      {
          throw e;
      }
  }

  @Override
  public String uncollectTaskByTaskId(long taskId, long uncollectorId)
  {
      try
      {
          if(!userClient.getUserExist(uncollectorId))
          {
              return "用户不存在！";
          }

          Task task = taskRepository.findById(taskId).orElse(null);
          if(task == null)
          {
              return "任务不存在！";
          }

          if(task.getStatus() == TaskStatus.DELETE)
          {
              return "该任务已被删除！";
          }

          TaskCollect taskCollect = taskCollectRepository.findByTaskAndCollectorId(task, uncollectorId);

          if(taskCollect == null)
          {
              return "用户未收藏该任务！";
          }

          task.setCollectedNumber(task.getCollectedNumber() - 1);
          taskCollectRepository.delete(taskCollect);

          return "取消收藏成功！";
      }
      catch (Exception e)
      {
          throw e;
      }
  }

  @Override
  public String accessTaskByTaskId(long taskId, long accessorId)
  {
      try
      {
          Task task = taskRepository.findById(taskId).orElse(null);
          if(task == null)
          {
              return "任务不存在！";
          }

          if(task.getOwner_id() == accessorId)
          {
              return "不能接取自己发布的任务！";
          }

          if(task.getStatus() == TaskStatus.DELETE)
          {
              return "该任务已被删除！";
          }

          else if(task.getStatus() == TaskStatus.REMOVE)
          {
              return "该任务已被下架！";
          }

          if(!userClient.getUserExist(accessorId))
          {
              return "用户不存在！";
          }

          if(taskAccessRepository.existsByAccessorIdAndTask(accessorId, task))
          {
              return "用户已经接取该任务！";
          }

          if(!task.accessNotFull())
          {
              return "该任务接取已达上限！";
          }

          if(task.getStatus() == TaskStatus.DELETE)
          {
              return "该任务已被删除！";
          }

          else if(task.getStatus() == TaskStatus.REMOVE)
          {
              return "该任务已被下架！";
          }

          TaskAccess taskAccess = new TaskAccess();
          taskAccess.setTask(task);
          taskAccess.setAccessorId(accessorId);
          taskAccess.setCreatedAt(LocalDateTime.now());

          taskAccessRepository.save(taskAccess);
          return "接取任务成功！";
      }
      catch (Exception e)
      {
          throw e;
      }
  }

  @Override
  public String unaccessTaskByTaskId(long taskId, long unaccessorId)
  {
      try
      {
          Task task = taskRepository.findById(taskId).orElse(null);
          if(task == null)
          {
              return "任务不存在！";
          }

          if(task.getStatus() == TaskStatus.DELETE)
          {
              return "该任务已被删除！";
          }

          if(!userClient.getUserExist(unaccessorId))
          {
              return "用户不存在！";
          }

          TaskAccess taskAccess = taskAccessRepository.findByTaskAndAccessorId(task, unaccessorId);

          if(taskAccess == null)
          {
              return "用户未接取该任务！";
          }

          taskAccessRepository.delete(taskAccess);
          return "取消接取任务成功！";
      }
      catch (Exception e)
      {
          throw e;
      }
  }

  @Override
  public String publishTask(long userId, String title, String description, long price, List<String> requestImages)
  {
      try
      {
          if(!userClient.getUserExist(userId))
          {
              return "用户不存在！";
          }

          String imagesURL = ImageUtil.mergeImages(requestImages);

          Task task = new Task();
          task.setTitle(title);
          task.setPrice(price);
          task.setOwner_id(userId);
          task.setImages(imagesURL);
          task.setDescription(description);
          task.setCreatedAt(LocalDateTime.now());

          taskRepository.save(task);

          return "任务发布成功！";
      }
      catch (Exception e)
      {
          throw e;
      }
  }
}
