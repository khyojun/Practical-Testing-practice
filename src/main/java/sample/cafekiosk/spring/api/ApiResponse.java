package sample.cafekiosk.spring.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

@Getter
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;


    public ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return of(status, status.name(), data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }

}
