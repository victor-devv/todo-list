package com.victor_devv.todo_list.controller.advice.jsend;

public class JSendError extends JSendResponse {
    public JSendError(String message, Integer code, Object data) {
        this.setStatus("error");
        this.setMessage(message);
        this.setCode(code);
        this.setData(data);
    }
}
