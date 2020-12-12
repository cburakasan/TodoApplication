package com.asan.todosecurity.Todo.Model;

import com.asan.todosecurity.Todo.Enum.Status;
import com.asan.todosecurity.User.Model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
@Data
@ToString(exclude = {"id"})
@EqualsAndHashCode(exclude = {"id"})
public class Todo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long id;

    @Column
    private String work;

    @Column
    private Date time;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user",foreignKey = @ForeignKey(name = "FK_User_Todo"))
    private User user;





}
