package com.victor_devv.todo_list.controller.advice;

import com.victor_devv.todo_list.controller.advice.jsend.JSendError;
import com.victor_devv.todo_list.controller.advice.jsend.JSendFail;
import com.victor_devv.todo_list.controller.advice.jsend.JSendResponse;
import com.victor_devv.todo_list.controller.advice.jsend.JSendSuccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseBuilder {
    public static ResponseEntity<JSendResponse> success(Object data) {
        return success(data, HttpStatus.OK);
    }

    public static ResponseEntity<JSendResponse> success(Object data, HttpStatus status) {
        return ResponseEntity.status(status).body(new JSendSuccess(data));
    }

    public static ResponseEntity<JSendResponse> fail(Object data, HttpStatus status) {
        return ResponseEntity.status(status).body(new JSendFail(data));
    }

    public static ResponseEntity<JSendResponse> error(String message, Integer code, Object data, HttpStatus status) {
        return ResponseEntity.status(status).body(new JSendError(message, code, data));
    }
}
