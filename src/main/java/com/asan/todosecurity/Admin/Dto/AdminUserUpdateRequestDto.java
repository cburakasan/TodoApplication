package com.asan.todosecurity.Admin.Dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = {"id"})
@EqualsAndHashCode(exclude = {"id"})
public class AdminUserUpdateRequestDto {

    private Long userId;

    private String username;

    private String password;

    private String email;

    private String gsm;

}
