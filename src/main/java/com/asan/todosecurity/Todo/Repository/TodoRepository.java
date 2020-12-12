package com.asan.todosecurity.Todo.Repository;

import com.asan.todosecurity.Todo.Model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {

    List<Todo> findAllByUserId(Long userId);

    List<Todo> findAllByTimeBetween( Date firstDate, Date lastDate);

}
