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

    /**
     * Admin veya User'ın kendi todolarını ekleyebildiği servis.
     * @param todoAddRequestDto
     * @return
     */
    @PostMapping("/add")
    public TodoAddResponseDto addTodo(@RequestBody TodoAddRequestDto todoAddRequestDto) {

        return todoService.addTodo(todoAddRequestDto);
    }

    /**
     * Admin veya User'ın kendi todolarını silebildiği servis.
     * @param todoDeleteRequestDto
     * @return
     */
    @PostMapping("/delete")
    public TodoDeleteResponseDto deleteTodo(@RequestBody TodoDeleteRequestDto todoDeleteRequestDto) {

        return todoService.deleteTodo(todoDeleteRequestDto);
    }

    /**
     * Admin veya User'ın kendi todolarını update ettiği
     * @param todoUpdateRequestDto
     * @return
     */
    @PostMapping("/update")
    public TodoUpdateResponseDto updateTodo(@RequestBody TodoUpdateRequestDto todoUpdateRequestDto) {

        return todoService.updateTodo(todoUpdateRequestDto);
    }

    /**
     * Userın kendi todolarını listelediği servis.
     * User todolarını listelerken tarih filtresi yapabilir,
     * Todolarını tarihe göre sıralayabilir.
     * @param todoListRequestDto
     * @return
     */
    @GetMapping("/user/getTodolistByUser")
    public TodoListResponseDto getTodolistByUser(@RequestBody TodoListRequestDto todoListRequestDto) {

        return todoService.getTodolistByUser(todoListRequestDto);
    }

    /**
     * Admin'in bütün todoları listelediği servis.
     * User todolarını listelerken tarih filtresi yapabilir,
     * Todolarını tarihe göre sıralayabilir.
     * @param todoListRequestDto
     * @return
     */
    @GetMapping("/admin/getTodolistByAdmin")
    public TodoListResponseDto getTodolistByAdminList(@RequestBody TodoListRequestDto todoListRequestDto) {

        return todoService.getTodolistByAdmin(todoListRequestDto);

    }

    /**
     * Admin görüntülediği todoların listesi içerisinde bir kullanıcıya ait todoları
     * tarih bazlı filtreleyerek görüntüleyebilir.
     * @param todoListRequestDto
     * @return
     */
    @GetMapping("/admin/getTodolistByAdminFilterUser")
    public TodoListResponseDto getTodolistByAdminFilterUser(@RequestBody TodoListRequestDto todoListRequestDto) {

        return todoService.getTodolistByAdminFilterUser(todoListRequestDto);

    }
}