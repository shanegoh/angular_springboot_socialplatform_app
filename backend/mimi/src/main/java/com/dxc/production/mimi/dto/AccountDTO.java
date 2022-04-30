package com.dxc.production.mimi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccountDTO {
    private long id;
    private String createdBy;
    private Date creationDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private int deleteFlag;
    private String email;
    private String name;
    private int role;
    private String username;
}
