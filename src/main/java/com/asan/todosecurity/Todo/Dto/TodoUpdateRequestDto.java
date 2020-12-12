package com.asan.todosecurity.Todo.Dto;

import com.asan.todosecurity.Todo.Enum.Status;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class TodoUpdateRequestDto implements Serializable {

    private Long todoId;

    private String work;

    private Date time;

    private Status status;

}
