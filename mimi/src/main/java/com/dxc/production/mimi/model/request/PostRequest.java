package com.dxc.production.mimi.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private long id;
    private String caption;
    private String hyperLink;
    private String mediaLink;
    private String username;
    private String name;
}
