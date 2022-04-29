package com.dxc.production.mimi.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "post")
public class PostEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 50)
    @CreatedBy
    private String username;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 500)
    private String hyperLink;

    @Column(length = 100)
    private String mediaLink;

    @Column(length = 50)
    private String caption;

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private int deleteFlag;
}
