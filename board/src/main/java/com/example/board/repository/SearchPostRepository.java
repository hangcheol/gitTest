package com.example.board.repository;

import com.example.board.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchPostRepository {

    Post search1();

    Page<Object[]> searchPage(Pageable pageable);
}
