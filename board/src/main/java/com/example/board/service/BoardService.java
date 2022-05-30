package com.example.board.service;

import com.example.board.dto.*;
import com.example.board.entity.AttachFile;
import com.example.board.entity.Board;
import com.example.board.entity.Post;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BoardService {
    
    // 게시판 삭제
    void removeWithPost(Long bid);

    // 게시판 목록
    PageResultDTO<BoardDTO, Object> getBoardList(PageRequestDTO pageRequestDTO);

    default BoardDTO entityToDTO(Board board) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bid(board.getBid())    //항철
                .bname(board.getBname())
                .build();
        return boardDTO;
    }

    BoardDTO getBoard(Long bid);

    String register(BoardDTO dto);

    default Map<String, Object> dtoToEntity(BoardDTO dto) {

        Map<String, Object> entityMap = new HashMap<>();    // HashMap 말고 다른 Map 으로 수정할 수 있으므로 이렇게 코드를 짜는게 좋다.

        // 게시판
        Board board = Board.builder()
//                .bid(dto.getBid())
                .bname(dto.getBname())
                .build();

        entityMap.put("board", board);

        return entityMap;    // dto 값을 받아 Map 으로 return
    }

    String getExistBoard(String bname);



    void remove(Long bid);

    void modify(BoardDTO boardDTO);

    BoardDTO get(String bname);
}
