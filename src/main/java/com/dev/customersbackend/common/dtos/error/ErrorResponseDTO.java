package com.dev.customersbackend.common.dtos.error;

import java.time.OffsetDateTime;
import java.util.List;

public class ErrorResponseDTO {
    private OffsetDateTime timestamps;
    private Integer status;
    private String message;
    private String path;
    private List<String> details;

    public ErrorResponseDTO(OffsetDateTime timestamps, Integer status, String message, String path, List<String> details) {
        this.timestamps = timestamps;
        this.status = status;
        this.message = message;
        this.path = path;
        this.details = details;
    }

    public OffsetDateTime getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(OffsetDateTime timestamps) {
        this.timestamps = timestamps;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
