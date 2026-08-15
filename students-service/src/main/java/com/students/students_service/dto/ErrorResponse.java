package com.students.students_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class ErrorResponse {

	public ErrorResponse(LocalDateTime now, int value, String reasonPhrase, String message2) {
		// TODO Auto-generated constructor stub
	}

	private LocalDateTime timestamp;
	private int status; // e.g., 404
	private String error; // e.g., "Not Found"
	private String message; // e.g., "Student not found with id: 2"

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// @Override
	// public String toString() {
	// return "ErrorResponse [timestamp=" + timestamp + ", status=" + status + ",
	// error=" + error + ", message="
	// + message + "]";
	// }
	public static Object builder() {
		// TODO Auto-generated method stub
		return null;
	}
}