package com.example.board.repository;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PostDTO;
import com.example.board.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insertBoard() {

        IntStream.rangeClosed(1,20).forEach(i -> {

            Board board = Board.builder()
                    .bname("테스트 게시판_" + i)
                    .build();

            boardRepository.save(board);
        });
    }

//    @Test
//    public void testGetBoardWithPost() {
//
//        List<Object[]> result = boardRepository.getBoardsWithPost("test board_1");
//
//        for (Object[] arr : result){
//            System.out.println(Arrays.toString(arr));
//        }
//    }

}