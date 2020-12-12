package com.asan.todosecurity.User.Dto;

public class LoginRequest {
    private Object username;
    private Object password;

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }
}
