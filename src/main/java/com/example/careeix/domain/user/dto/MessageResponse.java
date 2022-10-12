package com.example.careeix.domain.user.dto;



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
public class MessageResponse {

    private String message;

    public static LoginResponse from(String message) {
        return LoginResponse.builder()
                .message(message)
                .build();
    }

}
