package com.asan.todosecurity.Todo;

import lombok.Data;

@Data
public class TodoException extends Exception{
    private String message;

    public TodoException (String message, Throwable throwable){
        super(message, throwable);
        this.message = message;
    }

}
