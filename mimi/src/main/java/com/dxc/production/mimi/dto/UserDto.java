package com.dxc.production.mimi.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import java.util.Date;

@Data
public class UserDto {
    private String username;
    private String password;
    private String name;
    private int role;
}
