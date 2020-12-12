package com.asan.todosecurity.Todo.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TodoListRequestDto implements Serializable {

    private Long userId;
    private Boolean dateOrderByDesc;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date firsDate;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date lastDate;
}
