package com.victor_devv.todo_list.controller.advice.jsend;

public class JSendFail extends JSendResponse {
    public JSendFail(Object data) {
        this.setStatus("fail");
        this.setData(data);
    }
}
