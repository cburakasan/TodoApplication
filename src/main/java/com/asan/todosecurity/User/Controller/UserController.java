package com.asan.todosecurity.User.Controller;


import com.asan.todosecurity.Security.Config.JwtUtils;
import com.asan.todosecurity.Security.Config.RoleRepository;
import com.asan.todosecurity.Security.Model.ERole;
import com.asan.todosecurity.User.Dto.LoginRequest;
import com.asan.todosecurity.User.Dto.UserResponse;
import com.asan.todosecurity.User.Model.UserDetailsImpl;
import com.asan.todosecurity.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
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

    /**
     * User girişinin yapıldığı servis.
     * Username ve password ile giriş yapılır ve
     * alınan bir token üzerinden authenticate işlemi yapılır.
     * Veritabanına kayıt yapılırken Password şifrelenir.
     * @param loginRequest
     * @return
     */
    @PostMapping("/signin")
    public UserResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        UserResponse userResponse = new UserResponse();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        userResponse.setAdmin(false);
        if(roles.contains("ROLE_ADMIN")){
            userResponse.setAdmin(true);
        }
        userResponse.setToken(jwt);
        userResponse.setRoleList(roles);

        return userResponse;
    }
}
