package com.asan.todosecurity.Admin.Controller;

import com.asan.todosecurity.Admin.Dto.*;
import com.asan.todosecurity.Admin.Service.AdminService;
import com.asan.todosecurity.Security.Config.JwtUtils;
import com.asan.todosecurity.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Adminin bütün userları listelediği servis.
     * @param adminUserListRequestDto
     * @return
     */
    @RequestMapping("/userlist")
    public AdminUserListResponseDto userList(@RequestBody AdminUserListRequestDto adminUserListRequestDto) {

        return adminService.userList(adminUserListRequestDto);
    }

    /**
     * Adminin var olan user bilgilerini güncellediği servis.
     * @param adminUserUpdateRequestDto
     * @return
     */
    @PostMapping("/update")
    public AdminUserUpdateResponseDto userUpdate (@RequestBody AdminUserUpdateRequestDto adminUserUpdateRequestDto){
        return adminService.userUpdate(adminUserUpdateRequestDto);
    }

    /**
     * Adminin var olan bir user kaydını silebildiği servis.
     * @param adminUserDeleteRequestDto
     * @return
     */
    @PostMapping("/delete")
    public AdminUserDeleteResponseDto userDelete (@RequestBody AdminUserDeleteRequestDto adminUserDeleteRequestDto){

        return adminService.userDelete(adminUserDeleteRequestDto);
    }

    /**
     * Adminin user eklediği servis. Burada Kullanıcının admin veya user rolü belirlenir.
     * Veritabanında kayıtlı Role bulunmak zorundadır. Bu servis çağrılmadan önce veritabanına Role kaydı eklenmesi gerekir.
     * @param signUpRequest
     * @return
     */
    @PostMapping("/signup")
    public AdminUserSignUpResponseDto registerUser(@Valid @RequestBody AdminUserSignUpRequestDto signUpRequest) {
        return adminService.registerUser(signUpRequest);
    }

}
