package com.alexandre.quarkussocial.rest.dtos;

import lombok.Data;

import java.util.List;

@Data
public class FollowerPerUserResponse {
    private Integer followerCount;
    private List<FollowerResponse> content;
}
