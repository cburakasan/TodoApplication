package com.asan.todosecurity.Todo.Dto;

import com.asan.todosecurity.Todo.Enum.Status;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class TodoAddRequestDto implements Serializable {

    private String work;

    private Date time;

    private Status status;
}
