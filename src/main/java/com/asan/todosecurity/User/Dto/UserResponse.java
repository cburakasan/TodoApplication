package com.asan.todosecurity.User.Dto;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse
{
    private String token;
    private List<String> roleList;
    private boolean admin;
}
