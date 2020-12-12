package com.asan.todosecurity.Todo.Dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TodoAddResponseDto implements Serializable {

    private String message;
}
