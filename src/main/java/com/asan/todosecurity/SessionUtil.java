package com.asan.todosecurity;

import com.asan.todosecurity.User.Model.User;
import com.asan.todosecurity.User.Model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionUtil {

    HttpSession httpSession;

    public SessionUtil(HttpSession session){
            this.httpSession = session;

    }

    public UserDetailsImpl getUser(){
        UserDetailsImpl user = (UserDetailsImpl) httpSession.getAttribute("USER");
        return user;

    }

    public void setUser(UserDetailsImpl user){
        httpSession.setAttribute("USER", user);

    }


}
