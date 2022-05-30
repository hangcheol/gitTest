package com.example.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttachFileServiceTest {

    @Autowired
    private AttachFileService attachFileService;

    @Test
    public void testDeleteByPId() {

        Long aid = 1L;

        attachFileService.deleteByAId(aid);

    }
}