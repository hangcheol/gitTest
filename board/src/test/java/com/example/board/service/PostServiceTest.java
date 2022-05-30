package com.example.board.service;

import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.dto.PostDTO;
import com.example.board.entity.AttachFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    public void testRegister() {

        PostDTO dto = PostDTO.builder()
                .title("test register title")
                .content("test register content")
                .bname("test board_1")
                .build();

        Long pno = postService.register(dto);
    }

    @Test
    public void testGetList() {

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

//        String bname = "test board_1";
        Long bid = 1L;

        PageResultDTO<PostDTO, Object[]> result = postService.getList(pageRequestDTO, bid);

        for (PostDTO postDTO : result.getDtoList()) {
            System.out.println(postDTO);
        }
    }

    @Test
    public void testGet() {

        Long pid = 1L;

        PostDTO postDTO = postService.get(pid);

        System.out.println(postDTO);
    }

    @Test
    public void testDeleteByPId() {

        Long pid = 2L;

        postService.remove(pid);
    }

    @Test
    public void testModify() {

        PostDTO postDTO = PostDTO.builder()
                .pid(32L)
                .title("변경 테스트 제목")
                .content("변경 테스트 내용")
                .build();

        postService.modify(postDTO);
    }

    @Test
    public void testtest() {
        List<AttachFile> attachFiles = new ArrayList<>();
        Boolean tf = attachFiles.isEmpty();
    }
}