package com.students.students_service.dto;

import java.time.LocalDateTime;

public class ApiResponse {
    private String message;
    private boolean success;
    private LocalDateTime timestamp;

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.timestamp = LocalDateTime.now();
    }

    public void setMessage(String message) {
		this.message = message;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	// Getters and Setters
    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }
    public LocalDateTime getTimestamp() { return timestamp; }
}