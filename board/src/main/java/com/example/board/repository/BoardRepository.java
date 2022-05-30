package com.example.board.repository;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 게시판과 게시글 한번에 잘 불러와지나 테스트용
    @Query("SELECT b, p FROM Board b LEFT JOIN Post p ON p.board = b WHERE b.bid =:bid GROUP BY b")
    List<Object[]> getBoardsWithPost(@Param("bid") Long bid);

    @Query("SELECT b, p FROM Board b LEFT JOIN Post p ON p.board = b WHERE b.bid =:bid")
    Object getBoardWithPost(@Param("bid") Long bid);

    // 게시판 리스트
    @Query(value = "SELECT b FROM Board b", countQuery = "SELECT count(b) FROM Board b")
    Page<Object> getBoard(Pageable pageable);

    // 게시판 삭제
    @Modifying
    @Query("DELETE from Board b WHERE b.bid =:bid")
    void deleteBoardByBid(@Param("bid") Long bid);

    @Query("SELECT b.bid FROM Board b WHERE b.bname =:bname")
    String getBname(@Param("bname") String bname);

    @Query("SELECT b FROM Board b WHERE b.bname =:bname")
    Object getBoard(@Param("bname") String bname);

}
