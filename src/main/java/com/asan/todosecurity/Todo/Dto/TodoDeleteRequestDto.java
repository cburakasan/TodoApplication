package com.asan.todosecurity.Todo.Dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TodoDeleteRequestDto implements Serializable {

    private Long todoId;
}
