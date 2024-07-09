package com.renyibang.orderapi.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TaskDTO {
	long id;
	String title;
	String description;
	String images;
	LocalDateTime time;
}
