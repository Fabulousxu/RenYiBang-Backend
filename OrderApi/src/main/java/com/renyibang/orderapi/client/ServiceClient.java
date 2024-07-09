package com.renyibang.orderapi.client;

import com.renyibang.orderapi.dto.ServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service")
public interface ServiceClient {
	@GetMapping("/{id}")
	ServiceDTO getServiceById(@PathVariable("id") Long itemId);
}
