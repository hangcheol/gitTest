package com.example.board.repository;

import com.example.board.entity.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    // 첨부파일 삭제 (board)
    @Modifying
    @Query("DELETE from AttachFile a WHERE a.board.bid =:bid")
    void deleteByBid(@Param("bid") Long bid);

    // 첨부파일 삭제 (post)
    @Modifying
    @Query("DELETE from AttachFile a WHERE a.post.pid =:pid")
    void deleteByPId(@Param("pid") Long pid);

    // 첨부파일 삭제 (attach_file)
    @Modifying
    @Query("DELETE from AttachFile a WHERE a.aid =:aid")
    void deleteByAId(@Param("aid") Long aid);


}
