package com.asan.todosecurity.Admin.Service;


import com.asan.todosecurity.Admin.Dto.*;
import com.asan.todosecurity.Security.Config.RoleRepository;
import com.asan.todosecurity.Security.Model.ERole;
import com.asan.todosecurity.Security.Model.Role;
import com.asan.todosecurity.SessionUtil;
import com.asan.todosecurity.Todo.Repository.TodoRepository;
import com.asan.todosecurity.User.Dto.UserDto;
import com.asan.todosecurity.User.Exception.UserException;
import com.asan.todosecurity.User.Model.User;
import com.asan.todosecurity.User.Repository.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j
public class AdminService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public AdminUserListResponseDto userList(AdminUserListRequestDto adminUserListRequestDto) {

        AdminUserListResponseDto adminUserListResponseDto = new AdminUserListResponseDto();
        List<UserDto> userDtoList = new ArrayList<>();
        try {
            List<User> userList = userRepository.findAll();
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
            Long userId = adminUserUpdateRequestDto.getUserId();
            String usernameFromRequest = adminUserUpdateRequestDto.getUsername();
            String passwordFromRequest = adminUserUpdateRequestDto.getPassword();
            String emailFromRequest = adminUserUpdateRequestDto.getEmail();

            Optional<User> userOptional = userRepository.findById(userId);
            if (!userOptional.isPresent()) {
                throw new UserException("User bulunamadi", new Exception());
            }
            User user = userOptional.get();
            user.setUsername(usernameFromRequest);
            user.setPassword(passwordFromRequest);
            user.setEmail(emailFromRequest);
            user = userRepository.save(user);

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
            Long userId = adminUserDeleteRequestDto.getUserId();
            userRepository.deleteById(userId);
            adminUserDeleteResponseDto.setMesaj("Silme islemi basarili.");

        } catch (Exception exception) {
            String message = exception.getMessage();
            log.error(message + "silme isleminde bir hata olustu");
            adminUserDeleteResponseDto.setMesaj("silme isleminde bir hata olustu");
        }
        return adminUserDeleteResponseDto;
    }

    public AdminUserSignUpResponseDto registerUser(AdminUserSignUpRequestDto signUpRequest) {

        AdminUserSignUpResponseDto signupResponse = new AdminUserSignUpResponseDto();
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                String message = "Error: Username is already taken!";
                signupResponse.setMessage(message);
                return signupResponse;
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                String message = "Error: Email is already in use!";
                signupResponse.setMessage(message);
                return signupResponse;
            }

            User user = new User(signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()));

            Set<String> rolList = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (rolList == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                /**
                 * Veritabanında kayıtlı Role bulunmak zorundadır.
                 * Bu servis çağrılmadan önce veritabanına Role kaydı eklenmesi gerekir.
                 */
                rolList.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                    }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);
            signupResponse.setMessage("User registered successfully!");
        } catch (Exception exception) {
            String message = exception.getMessage();
            log.error(message + "User ekleme isleminde bir hata olustu");
            signupResponse.setMessage("User ekleme isleminde bir hata olustu");
        }

        return signupResponse;
    }
}
