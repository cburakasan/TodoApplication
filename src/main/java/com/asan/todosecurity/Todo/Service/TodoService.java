package com.asan.todosecurity.Todo.Service;


import com.asan.todosecurity.SessionUtil;
import com.asan.todosecurity.Todo.Dto.*;
import com.asan.todosecurity.Todo.Enum.Status;
import com.asan.todosecurity.Todo.Model.Todo;
import com.asan.todosecurity.Todo.Repository.TodoRepository;
import com.asan.todosecurity.Todo.TodoException;
import com.asan.todosecurity.User.Dto.UserDto;
import com.asan.todosecurity.User.Model.User;
import com.asan.todosecurity.User.Model.UserDetailsImpl;
import com.asan.todosecurity.User.Repository.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Log4j
public class TodoService {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepositroy;

    @Autowired
    SessionUtil sessionUtil;


    public TodoAddResponseDto addTodo(TodoAddRequestDto todoAddRequestDto) {
        TodoAddResponseDto todoAddResponseDto = new TodoAddResponseDto();
        try {

            UserDetailsImpl user1 = sessionUtil.getUser();
            String workFromRequest = todoAddRequestDto.getWork();
            Date timeFromRequest = todoAddRequestDto.getTime();
            Status statusFromRequest = todoAddRequestDto.getStatus();

            Optional<User> userOptional = userRepositroy.findById(user1.getId());
            if (!userOptional.isPresent()) {
                throw new TodoException("User bulunamadi", new Exception());
            }
            User user = userOptional.get();

            Todo todo = new Todo();

            todo.setWork(workFromRequest);
            todo.setStatus(statusFromRequest);
            todo.setTime(timeFromRequest);
            todo.setUser(user);

            todo = todoRepository.save(todo);

            todoAddResponseDto.setMessage("Todo olusturuldu.");
            log.info("Todo olusturuldu");
            return todoAddResponseDto;
        } catch (TodoException todoException) {
            String message = todoException.getMessage();
            log.error(message);
            todoAddResponseDto.setMessage(message);
            return todoAddResponseDto;
        } catch (Exception e) {
            String message = e.getMessage();
            String hataMesaji = "Todo olusturulurken bir hata olustu.";
            log.error(hataMesaji + " ex:" + message);
            todoAddResponseDto.setMessage(hataMesaji);
            return todoAddResponseDto;
        }
    }

    public TodoDeleteResponseDto deleteTodo(TodoDeleteRequestDto todoDeleteRequestDto) {

        TodoDeleteResponseDto todoDeleteResponseDto = new TodoDeleteResponseDto();

        try {
            Long todoId = todoDeleteRequestDto.getTodoId();
            todoRepository.deleteById(todoId);

            todoDeleteResponseDto.setMessage("Successfully");
            log.info("Todo silindi.");
            return todoDeleteResponseDto;
        } catch (Exception exception) {
            String message = exception.getMessage();
            String hataMesaji = "Todo silinirken bir hata olustu.";
            log.error(hataMesaji + " ex:" + message);
            todoDeleteResponseDto.setMessage(hataMesaji);
            return todoDeleteResponseDto;

        }
    }

    public TodoUpdateResponseDto updateTodo(TodoUpdateRequestDto todoUpdateRequestDto) {

        TodoUpdateResponseDto todoUpdateResponseDto = new TodoUpdateResponseDto();

        try {
            Long todoIdFromRequest = todoUpdateRequestDto.getTodoId();
            String workFromRequest = todoUpdateRequestDto.getWork();
            Date timeFromRequest = todoUpdateRequestDto.getTime();
            Status statusFromRequest = todoUpdateRequestDto.getStatus();

            Optional<Todo> todoOptional = (Optional<Todo>) todoRepository.findById(todoIdFromRequest);
            if (!todoOptional.isPresent()) {
                throw new TodoException("Todo bulunamadi", new Exception());
            }

            Todo todo = todoOptional.get();
            todo.setWork(workFromRequest);
            todo.setStatus(statusFromRequest);
            todo.setTime(timeFromRequest);

            todo = todoRepository.save(todo);

            todoUpdateResponseDto.setMessage("Successfully");
            log.info("Todo guncelleme basarili");
            return todoUpdateResponseDto;
        } catch (TodoException exception) {
            String message = exception.getMessage();
            log.error(message);
            todoUpdateResponseDto.setMessage(message);
            return todoUpdateResponseDto;
        } catch (Exception exception) {
            String message = exception.getMessage();
            String hataMesaji = "Todo guncellenirken bir hata olustu.";
            log.error(hataMesaji + " ex:" + message);
            todoUpdateResponseDto.setMessage(hataMesaji);
            return todoUpdateResponseDto;
        }
    }

    public TodoListResponseDto getTodolistByUser(TodoListRequestDto todoListRequestDto) {
        TodoListResponseDto todoListResponseDto = new TodoListResponseDto();
        List<TodoDto> todoDtoList = new ArrayList<>();

        try {
            UserDetailsImpl userDetails = sessionUtil.getUser();
            Long userId = userDetails.getId();
            Boolean dateOrderByDesc = todoListRequestDto.getDateOrderByDesc();

            Optional<User> userOptional = userRepositroy.findById(userId);
            User user = userOptional.get();

            List<Todo> todoList = null;
            todoList = todoRepository.findAllByUserId(user.getId());

            for (Todo todo : todoList) {
                Long todoId = todo.getId();
                String work = todo.getWork();
                Date time = todo.getTime();
                Status status = todo.getStatus();

                TodoDto todoDto = new TodoDto();
                todoDto.setId(todoId);
                todoDto.setWork(work);
                todoDto.setTime(time);
                todoDto.setStatus(status);
                todoDtoList.add(todoDto);
            }

            if (dateOrderByDesc != null && Boolean.TRUE.equals(dateOrderByDesc)) {
                todoDtoList.sort(Comparator.comparing(TodoDto::getTime).reversed());//desc sıralama
            } else if (dateOrderByDesc != null &&  Boolean.FALSE.equals(dateOrderByDesc)) {
                todoDtoList.sort(Comparator.comparing(TodoDto::getTime));//asc sıralama
            }

            todoListResponseDto.setTodoDtoList(todoDtoList);
            log.info("Todo listeleme basarili");
            return todoListResponseDto;

        } catch (Exception exception) {
            String message = exception.getMessage();
            String hataMesaji = "Todo listesi çekilirken bir hata olustu.";
            log.error(hataMesaji + " ex:" + message);
            todoListResponseDto.setMessage(hataMesaji);
            return todoListResponseDto;

        }
    }

    public TodoListResponseDto getTodolistByAdmin(TodoListRequestDto todoListRequestDto) {
        TodoListResponseDto todoListResponseDto = new TodoListResponseDto();
        List<TodoDto> todoDtoList = new ArrayList<>();

        try {
            Boolean dateOrderByDesc = todoListRequestDto.getDateOrderByDesc();
            Date firsDate = todoListRequestDto.getFirsDate();
            Date lastDate = todoListRequestDto.getLastDate();
            List<Todo> todoList = null;
            if(firsDate !=null && lastDate != null){
               todoList =  todoRepository.findAllByTimeBetween(firsDate, lastDate);
            }else {
                todoList = todoRepository.findAll();

            }


            for (Todo todo : todoList) {
                Long todoId = todo.getId();
                String work = todo.getWork();
                Date time = todo.getTime();
                Status status = todo.getStatus();
                User user = todo.getUser();
                UserDto userDto = new UserDto();
                userDto.setId(user.getId());
                userDto.setUsername(user.getUsername());

                TodoDto todoDto = new TodoDto();
                todoDto.setId(todoId);
                todoDto.setWork(work);
                todoDto.setTime(time);
                todoDto.setStatus(status);
                todoDto.setUserDto(userDto);
                todoDtoList.add(todoDto);
            }

            if (dateOrderByDesc != null && Boolean.TRUE.equals(dateOrderByDesc)) {
                todoDtoList.sort(Comparator.comparing(TodoDto::getTime).reversed());//desc sıralama
            } else if (dateOrderByDesc != null &&  Boolean.FALSE.equals(dateOrderByDesc)) {
                todoDtoList.sort(Comparator.comparing(TodoDto::getTime));//asc sıralama
            }

            todoListResponseDto.setTodoDtoList(todoDtoList);
            log.info("Todo listeleme basarili");
            return todoListResponseDto;

        } catch (Exception exception) {
            String message = exception.getMessage();
            String hataMesaji = "Todo listesi çekilirken bir hata olustu.";
            log.error(hataMesaji + " ex:" + message);
            todoListResponseDto.setMessage(hataMesaji);
            return todoListResponseDto;

        }
    }

    public TodoListResponseDto getTodolistByAdminFilterUser(TodoListRequestDto todoListRequestDto) {
        TodoListResponseDto todoListResponseDto = new TodoListResponseDto();
        List<TodoDto> todoDtoList = new ArrayList<>();

        try {
            Boolean dateOrderByDesc = todoListRequestDto.getDateOrderByDesc();
            Long userId = todoListRequestDto.getUserId();

            List<Todo> todoList = todoRepository.findAllByUserId(userId);

            for (Todo todo : todoList) {
                Long todoId = todo.getId();
                String work = todo.getWork();
                Date time = todo.getTime();
                Status status = todo.getStatus();
                User user = todo.getUser();
                UserDto userDto = new UserDto();
                userDto.setId(user.getId());
                userDto.setUsername(user.getUsername());

                TodoDto todoDto = new TodoDto();
                todoDto.setId(todoId);
                todoDto.setWork(work);
                todoDto.setTime(time);
                todoDto.setStatus(status);
                todoDto.setUserDto(userDto);
                todoDtoList.add(todoDto);
            }

            if (dateOrderByDesc != null && Boolean.TRUE.equals(dateOrderByDesc)) {
                todoDtoList.sort(Comparator.comparing(TodoDto::getTime).reversed());//desc sıralama
            } else if (dateOrderByDesc != null &&  Boolean.FALSE.equals(dateOrderByDesc)) {
                todoDtoList.sort(Comparator.comparing(TodoDto::getTime));//asc sıralama
            }

            todoListResponseDto.setTodoDtoList(todoDtoList);
            log.info("Todo listeleme basarili");
            return todoListResponseDto;

        } catch (Exception exception) {
            String message = exception.getMessage();
            String hataMesaji = "Todo listesi çekilirken bir hata olustu.";
            log.error(hataMesaji + " ex:" + message);
            todoListResponseDto.setMessage(hataMesaji);
            return todoListResponseDto;

        }
    }
}
