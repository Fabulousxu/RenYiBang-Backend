package com.renyibang.global.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ServiceDTO {
    long id;
    String title;
    String description;
    String images;
    LocalDateTime time;

    public ServiceDTO(long id, String title, String description, String images, LocalDateTime time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.images = images;
        this.time = time;
    }
}
