package com.asan.todosecurity.Todo.Dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TodoListResponseDto implements Serializable {

    private List<TodoDto> todoDtoList;

    private String message;
}
