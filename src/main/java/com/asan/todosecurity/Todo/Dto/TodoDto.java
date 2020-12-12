package com.asan.todosecurity.Todo.Dto;

import com.asan.todosecurity.Todo.Enum.Status;
import com.asan.todosecurity.User.Dto.UserDto;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class TodoDto implements Serializable {

    private Long id;
    private String work;
    private Date time;
    private Status status;
    private UserDto userDto;



}
