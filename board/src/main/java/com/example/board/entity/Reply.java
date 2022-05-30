package com.example.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"board", "post"})
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    private String reply;   // 댓글 내용

    private Long rgroup;    // 부모 댓글 rid
    private Long depth;     // 대댓글 여부(0은 댓글, 1은 대댓글)

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
