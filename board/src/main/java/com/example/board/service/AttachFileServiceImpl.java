package com.example.board.service;

import com.example.board.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AttachFileServiceImpl implements AttachFileService{

    @Autowired
    private final AttachFileRepository attachFileRepository;

    @Transactional
    @Override
    public void deleteByAId(Long aid) {

        attachFileRepository.deleteByAId(aid);
    }
}
