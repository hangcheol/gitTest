package com.example.board.repository;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PostDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void insertPost() {

        Board board = Board.builder().bid(1L).build();

        IntStream.rangeClosed(1,30).forEach(i -> {

            Post post = Post.builder()
                    .title("test post title_" + i)
                    .content("test post content_" + i)
                    .board(board)
                    .build();

            postRepository.save(post);

        });
    }

    @Test
    public void testRead() {

        Optional<Post> result = postRepository.findById(1L);

        Post post = result.get();

        System.out.println("post= " + post);
        System.out.println(post.getBoard());

    }

    @Test
    public void testGetPostWithAttachFile() {

        List<Object[]> result = postRepository.getPostWithAttachFile(1L);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void testGetPostByPId() {

        Object result = postRepository.getPostByPId(9L);

        Object[] arr = (Object[]) result;

        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testGetPostWithBoard() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("pid").descending());

//        String bname = "test board_1";

        Long bid = 8L;
        Page<Object[]> result = postRepository.getPostWithBoard(bid, pageable);

        result.get().forEach (row -> {
            Object[] arr = (Object[]) row;
            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    public void testGetPostWithAll() {

        Long pid = 2L;
        List<Object[]> result = postRepository.getPostWithAll(pid);

        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }

    }

    @Test
    public void testSerch1() {

    }

//    @Test
//    public void testGetBNameByPId() {
//
//        Long pid = 9L;
//        String bname = postRepository.getBNameByPId(pid);
//
//        System.out.println(bname);
//    }
}