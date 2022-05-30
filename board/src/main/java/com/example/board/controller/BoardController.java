package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.PageRequestDTO;
import com.example.board.service.BoardService;
import com.example.board.service.ValidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor // 자동으로 주입
public class BoardController {

    private final BoardService boardService;
    private final ValidService vaildService;

    // 게시판 목록
    @GetMapping("/list")
    public void list(@ModelAttribute PageRequestDTO pageRequestDTO, Model model) {

        log.info("test: " + boardService.getBoardList(pageRequestDTO));

        model.addAttribute("result", boardService.getBoardList(pageRequestDTO));
    }

    // 각 게시판의 게시글 목록
    @GetMapping("/read")
    public String read(@RequestParam Map<String, Object> param, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {

        String bname = (String)param.get("bname");
        Long bid = (Long) param.get("bid");

        BoardDTO boardDTO = boardService.getBoard(bid);

        model.addAttribute("dto", boardDTO);

        return "/post/list";
    }

    // 게시판 등록 페이지
    @GetMapping("/register")
    public void register() {
    }

    // 게시판 등록
    @PostMapping("/register")
    public String registerBoard(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // -------------------------------------------------------
        // 게시판명 예외 처리
        if (boardService.getExistBoard(boardDTO.getBname()) != null) {
            bindingResult.addError(new FieldError("dto", "bname", "이미 존재하는 게시판입니다."));
        }

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", boardDTO);

            Map<String, String> validatorResult = vaildService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/board/register";
        }
        // -------------------------------------------------------

        String bname = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("msg", bname);  // addFlashAttribute 로 전달한 값은 url 뒤에 붙지 않고, 리프레시하면 데이터가 소멸하는 일회성.

        return "redirect:/board/list"; // 등록 완료되면 목록으로 이동
    }

    // 게시글 조회(+ 수정) 페이지
    @GetMapping({"/modify"})
    public void readBoard(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, @RequestParam HashMap<String, String> param, Model model) {

        String bname = param.get("bname");
//        String
        BoardDTO boardDTO = boardService.get(bname);

        model.addAttribute("dto", boardDTO);

        log.info("---------------------------------------------------------");
        log.info("postDTO: {}", boardDTO);
        log.info("---------------------------------------------------------");
    }

    // 게시글 수정
    @PostMapping("/modify")
    public String modify(@Valid BoardDTO boardDTO, BindingResult bindingResult, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes, Model model) {
        
        log.info("---------------------------------------------------------");
        log.info("dto: {}", boardDTO);
        log.info("---------------------------------------------------------");

        // 게시판명 예외 처리
        if (boardService.getExistBoard(boardDTO.getBname()) != null) {
            bindingResult.addError(new FieldError("dto", "bname", "이미 존재하는 게시판입니다."));
        }

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", boardDTO);

            Map<String, String> validatorResult = vaildService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/board/modify";
        }

        boardService.modify(boardDTO);

        redirectAttributes.addAttribute("page", requestDTO.getPage());

        redirectAttributes.addAttribute("bname", boardDTO.getBname());

        return "redirect:/board/modify";
    }

    // 게시글 삭제
    @PostMapping("/remove")
    public String remove(Long bid, RedirectAttributes redirectAttributes) {

        boardService.remove(bid);
        log.info(String.valueOf(bid));

        redirectAttributes.addFlashAttribute("msg", bid);

        return "redirect:/board/list";
    }
}
