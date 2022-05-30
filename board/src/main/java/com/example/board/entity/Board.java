package com.example.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Table(name="board")
public class Board {    // 게시판

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;  // id

//    @Id
    private String bname;  // 게시판명

    public void changeBname(String bname) {

        this.bname = bname;
    }

}
