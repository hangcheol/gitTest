package com.example.board.service;

import com.example.board.dto.ReplyDTO;
import com.example.board.entity.Post;
import com.example.board.entity.Reply;
import com.example.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyDTO replyDTO) {

        if (replyDTO.getDepth() == null) {
            replyDTO.setDepth(0L);
        }

        if (replyDTO.getRgroup() == null) {
            replyDTO.setRgroup(0L);
        }

        Reply reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);

        return reply.getRid();
    }

    @Override
    public List<ReplyDTO> getList(Long pid) {

        List<Reply> result = replyRepository.getRepliesByPostOrderByRid(Post.builder().pid(pid).build());
        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);
        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rid) {
        replyRepository.deleteByRId(rid);
    }
}
