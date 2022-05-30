package com.example.board.repository;

import com.example.board.entity.Post;
import com.example.board.entity.QBoard;
import com.example.board.entity.QPost;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchPostRepository {

    public SearchBoardRepositoryImpl() {
        super(Post.class);
    }

    @Override
    public Post search1() {

        QPost post = QPost.post;

        JPQLQuery<Post> jpqlQuery = from(post);

//        jpqlQuery.select(post).where(post.pid.eq(1L));

        List<Post> result = jpqlQuery.fetch();

        return null;
    }

    @Override
    public Page<Object[]> searchPage(Pageable pageable) {



        return null;
    }
}
