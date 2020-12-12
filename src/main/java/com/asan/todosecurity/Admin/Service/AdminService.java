package com.asan.todosecurity.Admin.Service;


import com.asan.todosecurity.Admin.Dto.*;
import com.asan.todosecurity.SessionUtil;
import com.asan.todosecurity.Todo.Repository.TodoRepository;
import com.asan.todosecurity.User.Dto.UserDto;
import com.asan.todosecurity.User.Exception.UserException;
import com.asan.todosecurity.User.Model.User;
import com.asan.todosecurity.User.Model.UserDetailsImpl;
import com.asan.todosecurity.User.Repository.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j
public class AdminService {

    @Autowired
    UserRepository userRepositroy;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    SessionUtil sessionUtil;

    public AdminUserListResponseDto userList(AdminUserListRequestDto adminUserListRequestDto) {

        AdminUserListResponseDto adminUserListResponseDto = new AdminUserListResponseDto();
        List<UserDto> userDtoList = new ArrayList<>();
        try {
            List<User> userList = userRepositroy.findAll();
            for (User user : userList) {
                UserDto userDto = new UserDto();
                Long userId = user.getId();
                String username = user.getUsername();
                String email = user.getEmail();

                userDto.setId(userId);
                userDto.setUsername(username);
                userDto.setEmail(email);
                userDtoList.add(userDto);
            }
            adminUserListResponseDto.setUserDtoList(userDtoList);
            log.info("Listeleme basarili");

        } catch (Exception exception) {
            String message = exception.getMessage();
            log.error("User listeleme sirasinda bir hata olustu" + message);
            adminUserListResponseDto.setHataMesaji("User listeleme sirasinda bir hata olustu");
            return adminUserListResponseDto;
        }
        return adminUserListResponseDto;
    }

    public AdminUserUpdateResponseDto userUpdate(AdminUserUpdateRequestDto adminUserUpdateRequestDto) {

        AdminUserUpdateResponseDto adminUserUpdateResponseDto = new AdminUserUpdateResponseDto();
        try {
            UserDetailsImpl userDetails = sessionUtil.getUser();
            Long userId = userDetails.getId();
            String usernameFromRequest = adminUserUpdateRequestDto.getUsername();
            String gsmFromRequest = adminUserUpdateRequestDto.getGsm();
            String passwordFromRequest = adminUserUpdateRequestDto.getPassword();
            String emailFromRequest = adminUserUpdateRequestDto.getEmail();
            String adminFromRequest = adminUserUpdateRequestDto.getAdmin();

            Optional<User> userOptional = userRepositroy.findById(userId);
            if (!userOptional.isPresent()) {
                throw new UserException("User bulunamadi", new Exception());
            }
            User user = userOptional.get();
            user.setUsername(usernameFromRequest);
            user.setPassword(passwordFromRequest);
            user.setEmail(emailFromRequest);
            user = userRepositroy.save(user);

            adminUserUpdateResponseDto.setMesaj("User basariyla guncellendi.");
            log.info("User basariyla guncellendi.");
            return adminUserUpdateResponseDto;

        } catch (UserException exception) {
            String message = exception.getMessage();
            log.error(message);
            adminUserUpdateResponseDto.setMesaj(message);
            return adminUserUpdateResponseDto;
        } catch (Exception exception) {
            String message = exception.getMessage();
            log.error(message + "Update islemi sirasinda bir hata olustu.");
            adminUserUpdateResponseDto.setMesaj("Update islemi sirasinda bir hata olustu.");
            return adminUserUpdateResponseDto;
        }
    }

    public AdminUserDeleteResponseDto userDelete(AdminUserDeleteRequestDto adminUserDeleteRequestDto) {

        AdminUserDeleteResponseDto adminUserDeleteResponseDto = new AdminUserDeleteResponseDto();
        try {
            UserDetailsImpl userDetails = sessionUtil.getUser();
            Long userId = userDetails.getId();
            userRepositroy.deleteById(userId);
            adminUserDeleteResponseDto.setMesaj("Silme islemi basarili.");

        } catch (Exception exception) {
            String message = exception.getMessage();
            log.error(message + "silme isleminde bir hata olustu");
            adminUserDeleteResponseDto.setMesaj("silme isleminde bir hata olustu");
        }
        return adminUserDeleteResponseDto;
    }
}
