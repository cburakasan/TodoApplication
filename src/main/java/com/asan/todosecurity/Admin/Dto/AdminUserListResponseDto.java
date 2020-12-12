package com.asan.todosecurity.Admin.Dto;

import com.asan.todosecurity.User.Dto.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminUserListResponseDto implements Serializable {

    private List<UserDto> userDtoList;

    private String hataMesaji;

}
