package com.renyibang.orderapi.client;

import com.renyibang.orderapi.dto.TaskDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "task")
public interface TaskClient {
	@GetMapping("/{id}")
	TaskDTO getTaskById(@PathVariable("id") Long itemId);
}
