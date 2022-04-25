package com.dxc.production.mimi.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user")
public class UserEntity extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int role;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false)
    private int deleteFlag;
}
