package com.asan.todosecurity.User.Dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString(exclude = {"id"})
@EqualsAndHashCode(exclude = {"id"})
public class UserDto {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String gsm;

    private String admin;
}
