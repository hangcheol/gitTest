package com.example.board.dto;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class PostDTO {

    private Long pid;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private Long bid;
    private String bname;

    // 이미지 파일을 수집해 전달하기 위한 리스트
    @Builder.Default
    private List<AttachFileDTO> imageDTOList = new ArrayList<>();

    private Long replyCount;
}
