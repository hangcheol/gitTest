package com.example.board.repository;

import com.example.board.entity.Board;
import com.example.board.entity.Post;
import com.example.board.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsertReply() {

        Board board = Board.builder().bid(1L).build();
        Post post = Post.builder().pid(1L).build();

        IntStream.rangeClosed(1,10).forEach(i -> {

            Reply reply =  Reply.builder()
                    .reply("대댓글 테스트" + i)
                    .depth(1L)
                    .rgroup(1L)
                    .board(board)
                    .post(post)
                    .build();

            replyRepository.save(reply);

        });


    }

    @Test
    public void testInsertReplyReply() {

        Post post = Post.builder().pid(1L).build();

        Reply reply =  Reply.builder()
                .reply("대댓글 테스트")
                .depth(1L)
                .post(post)
                .build();

        replyRepository.save(reply);
    }

    @Test
    public void testGetRepliesByPostOrderByRId() {

        Board board = Board.builder().bname("test board_3").build();

        List<Reply> replyList = replyRepository.getRepliesByPostOrderByRid(Post.builder().pid(88L).board(board).build());

        replyList.forEach(reply -> System.out.println(reply));
    }

    @Test
    public void testDeleteByRid() {

        replyRepository.deleteByRId(11L);
    }
}