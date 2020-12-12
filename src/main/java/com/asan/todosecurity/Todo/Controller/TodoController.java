package com.asan.todosecurity.Todo.Controller;

import com.asan.todosecurity.Todo.Dto.*;
import com.asan.todosecurity.Todo.Service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    TodoService todoService;

    @PostMapping("/add")
    public TodoAddResponseDto addTodo(@RequestBody TodoAddRequestDto todoAddRequestDto) {

        return todoService.addTodo(todoAddRequestDto);
    }

    @PostMapping("/delete")
    public TodoDeleteResponseDto deleteTodo(@RequestBody TodoDeleteRequestDto todoDeleteRequestDto) {

        return todoService.deleteTodo(todoDeleteRequestDto);
    }

    @PostMapping("/update")
    public TodoUpdateResponseDto updateTodo(@RequestBody TodoUpdateRequestDto todoUpdateRequestDto) {

        return todoService.updateTodo(todoUpdateRequestDto);
    }

    @GetMapping("/user/getTodolistByUser")
    public TodoListResponseDto getTodolistByUser(@RequestBody TodoListRequestDto todoListRequestDto) {

        return todoService.getTodolistByUser(todoListRequestDto);

    }

    @GetMapping("/admin/getTodolistByAdminList")
    public TodoListResponseDto getTodolistByAdminList(@RequestBody TodoListRequestDto todoListRequestDto) {

        return todoService.getTodolistByAdmin(todoListRequestDto);

    }

    @GetMapping("/admin/getTodolistByAdminFilterUser")
    public TodoListResponseDto getTodolistByAdminFilterUser(@RequestBody TodoListRequestDto todoListRequestDto) {

        return todoService.getTodolistByAdminFilterUser(todoListRequestDto);

    }
}