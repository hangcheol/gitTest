package com.example.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class ReplyDTO {

    private Long rid;

    private String reply;

    private Long rgroup;
    private Long depth;

    private Long pid;
    private Long bid;
//    private String bname;

    public ReplyDTO() {
        this.rgroup = 0L;
        this.depth = 0L;
    }
}
