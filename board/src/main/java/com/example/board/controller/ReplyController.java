package com.example.board.controller;

import com.example.board.dto.ReplyDTO;
import com.example.board.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(value = "/post/{pid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByPost(@PathVariable("pid") Long pid) {
        return new ResponseEntity<>(replyService.getList(pid), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {

        Long rid = replyService.register(replyDTO);

        return new ResponseEntity<>(rid, HttpStatus.OK);
    }

    @DeleteMapping("/{rid}")
    public ResponseEntity<String> remove(@PathVariable("rid") Long rid) {

        log.info("RID:" + rid );
        replyService.remove(rid);

        return new ResponseEntity<>("success", HttpStatus.OK);

    }

    @PutMapping("/{rid}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {

        log.info(String.valueOf(replyDTO));
        replyService.modify(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);

    }
}
