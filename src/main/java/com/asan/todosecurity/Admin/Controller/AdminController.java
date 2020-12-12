package com.asan.todosecurity.Admin.Controller;

import com.asan.todosecurity.Admin.Dto.*;
import com.asan.todosecurity.Admin.Service.AdminService;
import com.asan.todosecurity.Security.Config.JwtUtils;
import com.asan.todosecurity.Security.Config.RoleRepository;
import com.asan.todosecurity.Security.Model.ERole;
import com.asan.todosecurity.Security.Model.Role;
import com.asan.todosecurity.User.Dto.UserResponse;
import com.asan.todosecurity.User.Model.User;
import com.asan.todosecurity.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

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
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @RequestMapping("/userlist")
    public AdminUserListResponseDto userList(@RequestBody AdminUserListRequestDto adminUserListRequestDto) {

        return adminService.userList(adminUserListRequestDto);
    }

    @PostMapping(value = "/add")
    public UserResponse userEkle(@RequestBody AdminUserAddRequestDto adminUserAddRequestDto) {

        return null;//adminService.userEkle(adminUserAddRequestDto);
    }

    @PostMapping("/update")
    public AdminUserUpdateResponseDto userUpdate (@RequestBody AdminUserUpdateRequestDto adminUserUpdateRequestDto){
        return adminService.userUpdate(adminUserUpdateRequestDto);
    }

    @PostMapping("/delete")
    public AdminUserDeleteResponseDto userDelete (@RequestBody AdminUserDeleteRequestDto adminUserDeleteRequestDto){

        return adminService.userDelete(adminUserDeleteRequestDto);
    }


    @PostMapping("/signup")
    public SignupResponse registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        SignupResponse signupResponse = new SignupResponse();

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

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
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

        return signupResponse;
    }
}
