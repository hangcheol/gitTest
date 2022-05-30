package com.example.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "board")
public class Post { // 게시글

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;      // id

    private String title;       // 제목
    @Column(length=1000)
    private String content;     // 내용
//    private Long attachFile;    // 첨부파일 갯수

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;     // 게시판

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
