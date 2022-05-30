package com.example.board.repository;

import com.example.board.entity.AttachFile;
import com.example.board.entity.Board;
import com.example.board.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttachFileRepositoryTest {

    @Autowired
    private AttachFileRepository attachFileRepository;

    @Test
    public void insertAttachFile() {

        Board board = Board.builder().bname("test board_2").build();

        Post post = Post.builder().pid(2L).build();

        IntStream.rangeClosed(1,5).forEach(i -> {
//            Post post = Post.builder().pid(Long.valueOf(i)).build();
            AttachFile attachFile = AttachFile.builder()
                    .imgName("test_file_" + i)
                    .uuid("test" + i)
                    .path("C:/upload/")
                    .post(post)
                    .board(board)
                    .build();

            attachFileRepository.save(attachFile);
        });
    }

}