package com.example.board.repository;

import com.example.board.entity.Post;
import com.example.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Reply r WHERE r.board.bid =:bid")
    void deleteByBid(@Param("bid")Long bid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Reply r WHERE r.post.pid =:pid")
    void deleteByPId(@Param("pid")Long pid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Reply r WHERE r.rid =:rid")
    void deleteByRId(@Param("rid")Long rid);

    List<Reply> getRepliesByPostOrderByRid(Post post);

}
