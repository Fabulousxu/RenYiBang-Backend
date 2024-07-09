package com.renyibang.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service")
public interface ServiceClient {
	// Order 模块的调用
	/*
	public class ServiceDTO {
		long id;
		String title;
		String description;
		String images;
		LocalDateTime time;
	}
	 */
	@GetMapping("/{id}")
	Object getServiceById(@PathVariable("id") Long itemId);
	//////////////////////
}
