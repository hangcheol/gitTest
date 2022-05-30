package com.example.board.controller;

import com.example.board.dto.PageRequestDTO;
import com.example.board.dto.PostDTO;

import com.example.board.service.PostService;
import com.example.board.service.ValidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/post")
@RequiredArgsConstructor // 자동으로 주입
public class PostController {

    private final PostService postService;
    private final ValidService validService;

    // 게시판 - 게시글 목록
    @GetMapping("/list")
    public void list(@ModelAttribute PageRequestDTO pageRequestDTO, @RequestParam HashMap<String, String> param, Model model) {

        String bname = param.get("bname");
        Long bid = Long.valueOf(param.get("bid"));

        log.info("bname: {}, bid: {}", bname, bid);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("getList", postService.getList(pageRequestDTO,bid));
        resultMap.put("bname", bname);
        resultMap.put("bid", bid);

//        model.addAttribute("result", postService.getList(pageRequestDTO, bname));
        model.addAttribute("result", resultMap);
    }

    // 게시글 작성 페이지
    @GetMapping("/register")
    public void register(PostDTO postDTO, Model model) {

        model.addAttribute("dto", postDTO);
    }

    // 게시글 작성
    @PostMapping("/register")
    public String registerPost(@Valid PostDTO postDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws UnsupportedEncodingException {

        String bid = String.valueOf(postDTO.getBid());

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", postDTO);

            Map<String, String> validatorResult = validService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/post/register";
        }

        Long pid = postService.register(postDTO);

        redirectAttributes.addFlashAttribute("msg", pid);  // addFlashAttribute 로 전달한 값은 url 뒤에 붙지 않고, 리프레시하면 데이터가 소멸하는 일회성.

        String bname = URLEncoder.encode(postDTO.getBname(), "UTF-8");
        bname = bname.replaceAll("\\+", "%20");
        log.info(bname);

        return "redirect:/post/list?bname=" + bname + "&bid=" + bid; // 등록 완료되면 목록으로 이동
    }

    // 게시글 조회(+ 수정) 페이지
    @GetMapping({"/read", "/modify"})
    public void read(Long pid, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {

        PostDTO postDTO = postService.get(pid);

        model.addAttribute("dto", postDTO);

        log.info("---------------------------------------------------------");
        log.info("postDTO: {}", postDTO);
        log.info("---------------------------------------------------------");
    }

    // 게시글 수정
    @PostMapping("/modify")
    public String modify(@Valid PostDTO dto, BindingResult bindingResult,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes, Model model) {
        log.info("---------------------------------------------------------");
        log.info("dto: {}", dto);

        String bid = String.valueOf(dto.getBid());

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);

            Map<String, String> validatorResult = validService.validateHandling(bindingResult);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/post/modify";
        }

        postService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());

        redirectAttributes.addAttribute("pid", dto.getPid());

        return "redirect:/post/read?bname=" + dto.getBname();
    }

    // 게시글 삭제
    @PostMapping("/remove")
    public String remove(Long pid, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {

        redirectAttributes.addFlashAttribute("msg", pid);

        String bname = URLEncoder.encode(postService.remove(pid), "UTF-8");
        bname = bname.replaceAll("\\+", "%20");
        log.info(bname);

        return "redirect:/post/list?bname=" + bname;
    }


}
