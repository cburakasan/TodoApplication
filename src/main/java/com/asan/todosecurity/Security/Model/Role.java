package com.asan.todosecurity.Security.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Roles")
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(length =20 )
    private ERole name;

    public Role(){

    }

    public Role(ERole name) {
        this.name = name;
    }


}
