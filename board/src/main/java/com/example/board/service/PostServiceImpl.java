package com.example.board.service;

import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PageResultDTO;
import com.example.board.dto.PostDTO;
import com.example.board.entity.AttachFile;
import com.example.board.entity.Board;
import com.example.board.entity.Post;
import com.example.board.repository.AttachFileRepository;
import com.example.board.repository.PostRepository;
import com.example.board.repository.ReplyRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final AttachFileRepository attachFileRepository;
    private final ReplyRepository replyRepository;
    // register 구현
    @Transactional
    @Override
    public Long register(PostDTO dto) {

        log.info("호출되는지?4");

        Map<String, Object> entityMap = dtoToEntity(dto);
        Post post = (Post) entityMap.get("post");   // 키가 post 인 값을 가져온다(게시글 정보)

        List<AttachFile> attachFileList = (List<AttachFile>) entityMap.get("imgList");  // 키가 imgList 인 값을 가져온다(첨부파일 정보)

        postRepository.save(post); // 저장

        // 첨부파일 있는 경우만 저장
        if (attachFileList != null) {

            attachFileList.forEach(attachFile -> {
                attachFileRepository.save(attachFile);
            });

        }

        return post.getPid();
    }

    // 게시물 페이지 처리 6
    @Override
    public PageResultDTO<PostDTO, Object[]> getList(PageRequestDTO pageRequestDTO, Long bid) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("pid").descending());

        Page<Object[]> result = postRepository.getPostWithBoard(bid, pageable);

        log.info("test -----------------------------------------1");
        result.getContent().forEach(arr -> {
            log.info(Arrays.toString(arr));
        });
        log.info("test -----------------------------------------1");

        Function<Object[], PostDTO> fn = (arr -> entityToDTO(
                (Post)arr[0],
                (Board)arr[1])
//                (List<AttachFile>)(Arrays.asList((AttachFile)arr[1])))
        );

        return new PageResultDTO<>(result, fn);
    }

    // 게시물 조회
    @Override
    public PostDTO get(Long pid) {

//        Object result = postRepository.getPostByPId(pid);
//
//        Object[] arr = (Object[]) result;
//
//        return entityToDTO((Post)arr[0], (Board)arr[1]);

        List<Object[]> result = postRepository.getPostWithAll(pid);

        Post post = (Post) result.get(0)[0];    // 게시글 엔티티가 맨 앞에 존재
        Board board = (Board) result.get(0)[1]; // 게시판
        Long replyCount = (Long) result.get(0)[3];
        log.info("-----------------------------");
        log.info(String.valueOf(board));
        log.info("-----------------------------");

        List<AttachFile> attachFileList = new ArrayList<>();    // 이미지파일 갯수만큼 필요

        AttachFile attachFiles = (AttachFile) result.get(0)[2];

        if (attachFiles != null) {
            result.forEach(arr -> {
                AttachFile attachFile = (AttachFile) arr[2];    // 첨부파일
                log.info("첨부파일 값: {}", attachFile);
                attachFileList.add(attachFile);
            });
        }
        return entitiesToDTO(post, board, attachFileList, replyCount);
    }

    // 게시물 삭제
    @Transactional
    @Override
    public String remove(Long pid) {

        String bname = postRepository.getBnameByPId(pid);

        // 댓글 삭제
        replyRepository.deleteByPId(pid);
        // 첨부파일 삭제
        attachFileRepository.deleteByPId(pid);
        // 게시글 삭제
        postRepository.deleteById(pid);

        return bname;
    }

    @Transactional
    @Override
    public void modify(PostDTO postDTO) {
        Post post = postRepository.getById(postDTO.getPid());

        post.changeTitle(postDTO.getTitle());
        post.changeContent(postDTO.getContent());

        postRepository.save(post);
    }
}
