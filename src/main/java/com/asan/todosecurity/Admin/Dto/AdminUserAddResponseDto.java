package com.asan.todosecurity.Admin.Dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminUserAddResponseDto implements Serializable {
    private String hataMessage;
    private Long id;
    private String username;
    private String password;
    private String email;
    private String gsm;
    private boolean admin;
}
