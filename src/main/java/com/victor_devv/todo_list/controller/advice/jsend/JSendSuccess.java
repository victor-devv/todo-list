package com.victor_devv.todo_list.controller.advice.jsend;

public class JSendSuccess extends JSendResponse {
    public JSendSuccess(Object data) {
        this.setStatus("success");
        this.setData(data);
    }
}
