package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PostDTO;
import com.example.board.dto.ReplyDTO;
import com.example.board.entity.Board;
import com.example.board.entity.Post;
import com.example.board.entity.Reply;

import java.util.List;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    List<ReplyDTO> getList(Long pid);

    void modify(ReplyDTO replyDTO);

    void remove(Long rid);

    default Reply dtoToEntity(ReplyDTO replyDTO) {

        Board board = Board.builder().bid(replyDTO.getBid()).build();
        Post post = Post.builder().pid(replyDTO.getPid()).build();

        Reply reply = Reply.builder()
                .rid(replyDTO.getRid())
                .reply(replyDTO.getReply())
                .rgroup(replyDTO.getRgroup())
                .depth(replyDTO.getDepth())
                .board(board)
                .post(post)
                .build();
        return reply;
    }

    default ReplyDTO entityToDTO(Reply reply) {

        ReplyDTO replyDTO = ReplyDTO.builder()
                .rid(reply.getRid())    // id
                .reply(reply.getReply())    // 댓글 내용
                .depth(reply.getDepth())
                .rgroup(reply.getRgroup())
                .bid(reply.getBoard().getBid())
                .pid(reply.getPost().getPid())
                .build();

        return replyDTO;
    }
}
