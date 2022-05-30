package com.example.board.service;

import com.example.board.dto.*;
import com.example.board.entity.AttachFile;
import com.example.board.entity.Board;
import com.example.board.entity.Post;
import com.example.board.entity.Reply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface PostService {

    Long register(PostDTO dto); // 게시글 등록

    default Map<String, Object> dtoToEntity(PostDTO dto) {

        Map<String, Object> entityMap = new HashMap<>();    // HashMap 말고 다른 Map 으로 수정할 수 있으므로 이렇게 코드를 짜는게 좋다.

        Board board = Board.builder()
                .bid(dto.getBid())
                .bname(dto.getBname()).build();  // 게시판

        // 게시글
        Post post = Post.builder()
                .pid(dto.getPid())
                .title(dto.getTitle())
                .content(dto.getContent())
                .board(board)
                .build();

        entityMap.put("post", post);
        
        // 게시글 내 이미지 파일
        List<AttachFileDTO> attachFileDTOList = dto.getImageDTOList();

                // 이미지 파일 정상일 경우 처리
        if (attachFileDTOList != null && attachFileDTOList.size() > 0) {

            List<AttachFile> attachFileList = attachFileDTOList.stream().map(attachFileDTO -> {

                AttachFile attachFile = AttachFile.builder()
                        .imgName(attachFileDTO.getImgName())
                        .path(attachFileDTO.getPath())
                        .uuid(attachFileDTO.getUuid())
                        .board(board)
                        .post(post)
                        .build();
                return attachFile;
            }).collect(Collectors.toList());

            entityMap.put("imgList", attachFileList);
        }

        return entityMap;    // dto 값을 받아 Map 으로 return
    }

    // 게시물 페이지 처리 3
    PageResultDTO<PostDTO, Object[]> getList(PageRequestDTO pageRequestDTO, Long bid);

    // 게시물 페이지 처리 4
    default PostDTO entityToDTO(Post post, Board board) {

        PostDTO postDTO = PostDTO.builder()
                .pid(post.getPid())
                .title(post.getTitle())
                .content(post.getContent())
                .bname(board.getBname())
                .build();
        return postDTO;
    }

    default PostDTO entitiesToDTO(Post post, Board board, List<AttachFile> attachFiles, Long replyCount) {

        PostDTO postDTO = PostDTO.builder()
                .pid(post.getPid())
                .title(post.getTitle())
                .content(post.getContent())
                .bid(board.getBid())
                .bname(board.getBname())
                .replyCount(replyCount)
                .build();

        if (!attachFiles.isEmpty()) {

            List<AttachFileDTO> attachFileDTOList = attachFiles.stream().map(attachFile -> {
                return AttachFileDTO.builder()
                        .imgName(attachFile.getImgName())
                        .path(attachFile.getPath())
                        .uuid(attachFile.getUuid())
                        .build();
            }).collect(Collectors.toList());

            postDTO.setImageDTOList(attachFileDTOList);

        }

        return postDTO;

    }

    // 게시물 조회
    PostDTO get(Long pid);

    // 게시글 삭제
    String remove(Long pid);

    // 게시글 수정
    void modify(PostDTO postDTO);
}

