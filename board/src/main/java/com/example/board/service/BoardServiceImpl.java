package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.entity.Board;
import com.example.board.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final AttachFileRepository attachFileRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    @Override
    public void removeWithPost(Long bid) {
        
        attachFileRepository.deleteByBid(bid);     // 첨부파일 삭제
        postRepository.deleteByBid(bid);   // 게시글 삭제
//        boardRepository.deleteById(bid);     // 게시판 삭제
    }

    @Override
    public PageResultDTO<BoardDTO, Object> getBoardList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("bname").descending());

        Page<Object> result = boardRepository.getBoard(pageable);

        result.getContent().forEach(val -> {
            log.info("페이지 값: {}", val);
        });

        Function<Object, BoardDTO> fn = (bname-> entityToDTO((Board)bname));
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO getBoard(Long bid) {

        Object result = boardRepository.getBoardWithPost(bid);

        Object[] arr = (Object[]) result;

        return entityToDTO((Board)arr[0]);
    }

    @Transactional
    @Override
    public String register(BoardDTO dto) {

        Map<String, Object> entityMap = dtoToEntity(dto);
        Board board = (Board) entityMap.get("board");   // 키가 post 인 값을 가져온다(게시글 정보)

        boardRepository.save(board); // 저장

        return board.getBname();
    }

    @Override
    public String getExistBoard(String bname) {

        return boardRepository.getBname(bname);
    }

    @Transactional
    @Override
    public void remove(Long bid) {

        // 댓글 삭제
        replyRepository.deleteByBid(bid);
        // 첨부파일 삭제
        attachFileRepository.deleteByBid(bid);
        // 게시글 삭제
        postRepository.deleteByBid(bid);
        // 게시판 삭제
        boardRepository.deleteBoardByBid(bid);

    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        Board board = boardRepository.getById(boardDTO.getBid());

        board.changeBname(boardDTO.getBname());

        boardRepository.save(board);
    }

    @Override
    public BoardDTO get(String bname) {

        Object result = boardRepository.getBoard(bname);

//        Object[] arr = (Object[]) result;

//        return entityToDTO((Board)arr[0]);
        return entityToDTO((Board)result);
    }

}
