package com.example.board.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long bid;

    @NotBlank(message = "게시판명은 공란일 수 없습니다.")
    private String bname;

}
