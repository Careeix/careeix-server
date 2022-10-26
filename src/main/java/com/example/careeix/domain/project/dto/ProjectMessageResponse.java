package com.example.careeix.domain.project.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "메시지 전달 응답 객체")
public class ProjectMessageResponse {
    private String message;

    public static PostProjectResponse from(String message) {
        return PostProjectResponse.builder()
//                .message(message)
                .build();
    }

}
