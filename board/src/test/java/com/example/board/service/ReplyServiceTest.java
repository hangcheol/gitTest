package com.example.board.service;

import com.example.board.dto.ReplyDTO;
import com.example.board.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyServiceTest {

    @Autowired
    private ReplyService replyService;

    @Test
    public void testGetList() {

        Long pid = 55L;

        List<ReplyDTO> replyDTOList = replyService.getList(pid);

        replyDTOList.forEach(replyDTO -> System.out.println(replyDTO));

    }

}