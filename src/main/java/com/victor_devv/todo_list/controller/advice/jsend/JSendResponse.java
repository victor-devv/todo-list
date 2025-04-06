package com.victor_devv.todo_list.controller.advice.jsend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JSendResponse {
    private String status;
    private Object data;
    private String message;
    private Integer code;
}

