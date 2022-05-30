package com.example.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


// 게시물 페이지 처리 2
// 제네릭 타입으로 dto 와 entity 를 받는다
@Data
public class PageResultDTO<DTO, EN> {

    private List<DTO> dtoList;

    private int totalPage;

    private int page;

    private int size;

    private int start, end;

    private boolean prev, next;

    private List<Integer> pageList;

    // Function 은 Entity 를 DTO 로 변환하는 기능
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {

        dtoList = result.stream().map(fn).collect(Collectors.toList()); // https://fors.tistory.com/453

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        // 시작 번호 계산을 위해 tempEnd 먼저 계산산
        int tempEnd = (int)(Math.ceil(page/ 10.0)) * 10;

        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
