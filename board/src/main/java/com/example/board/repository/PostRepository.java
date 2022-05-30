package com.example.board.repository;

import com.example.board.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // attach file 과 함께 post 조회
    @Query("SELECT p, a FROM Post p LEFT JOIN AttachFile a ON a.post = p WHERE p.pid =:pid")
    List<Object[]> getPostWithAttachFile(@Param("pid") Long pid);

    // board 값과 함께 post 조회
    @Query(value = "SELECT p, b FROM Post p LEFT JOIN p.board b WHERE p.board.bid=:bid", countQuery = "SELECT count(p) FROM Post p WHERE p.board.bid=:bid")
    Page<Object[]> getPostWithBoard(@Param("bid") Long bid ,Pageable pageable);

    // pid 로 조회
    @Query("SELECT p, b From Post p LEFT JOIN p.board b WHERE p.pid =:pid")
    Object getPostByPId(@Param("pid") Long pid);

    // pid 로 bid 조회 (게시글의 게시판 확인)
    @Query("SELECT p.board.bname From Post p WHERE p.pid =:pid")
    String getBnameByPId(@Param("pid") Long pid);


    // id 로 값 전체 조회
//    @Query("SELECT p, b, a FROM Post p LEFT JOIN p.board b LEFT OUTER JOIN AttachFile a on a.post = p WHERE p.pid =:pid GROUP BY a")
//    List<Object[]> getPostWithAll(@Param("pid") Long pid);

    @Query("SELECT p, b, a, count(r.rid)" +
            " FROM Post p" +
            " LEFT JOIN p.board b" +
            " LEFT OUTER JOIN AttachFile a on a.post = p" +
            " LEFT OUTER JOIN Reply r on r.post = p and r.depth = 0" +
            " WHERE p.pid =:pid GROUP BY a")
    List<Object[]> getPostWithAll(@Param("pid") Long pid);

    // 게시글 삭제 (board)
    @Modifying
    @Query("DELETE from Post p where p.board.bid =:bid")
    void deleteByBid(@Param("bid") Long bid);

    // 게시글 삭제 (post)
    @Modifying
    @Query("DELETE from Post p where p.pid =:pid")
    void deleteByPId(@Param("pid") Long pid);

}
