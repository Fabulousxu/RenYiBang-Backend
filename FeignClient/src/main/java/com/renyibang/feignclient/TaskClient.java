package com.renyibang.feignclient;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TaskApi")
public interface TaskClient {
  @GetMapping("api/task/{taskId}/ownerId")
  JSONObject getTaskOwnerId(@PathVariable long taskId);
}
