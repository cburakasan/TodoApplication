package com.asan.todosecurity.Admin.Dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class AdminUserAddRequestDto implements Serializable {

    private String username;

    private String password;

    private String email;

    private String gsm;





}
