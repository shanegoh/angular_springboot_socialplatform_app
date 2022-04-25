package com.dxc.production.mimi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private long id;
    private String username;
    private String name;
    private String hyperLink;
    private String mediaLink;
    private String caption;
    private long viewCount;
}
