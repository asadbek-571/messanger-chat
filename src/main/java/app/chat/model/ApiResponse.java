package app.chat.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Setter
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {

    @JsonProperty("data")
    private T data;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("totalCount")
    private Long totalCount;

    public ApiResponse(T data) {
        this.data = data;
        this.success = true;
        this.errorMessage = "";
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(T data, Long totalCount) {
        this.data = data;
        this.success = true;
        this.totalCount = totalCount;
        this.errorMessage = "";
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(String errorMessage) {
        this.data = null;
        this.success = false;
        this.errorMessage = errorMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(T data, String errorMessage) {
        this.data = data;
        this.success = false;
        this.errorMessage = errorMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse() {
        this.success = true;
        this.errorMessage = "";
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ResponseEntity<ApiResponse<T>> response(T data) {
        return ResponseEntity.ok(new ApiResponse<>(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> response(T data, Long totalCount) {
        return ResponseEntity.ok(new ApiResponse<>(data, totalCount));
    }

    public static <T> ResponseEntity<ApiResponse<T>> response(ApiResponse<T> responseData, HttpStatus status) {
        return new ResponseEntity<>(responseData, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> response(String errorMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ApiResponse<>(null, errorMessage), httpStatus);
    }

    public static <T> ResponseEntity<ApiResponse<T>> response(T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ApiResponse<>(data), httpStatus);
    }

    public static <T> ResponseEntity<ApiResponse<T>> response(T data, Long totalCount, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ApiResponse<>(data, totalCount), httpStatus);
    }
}
