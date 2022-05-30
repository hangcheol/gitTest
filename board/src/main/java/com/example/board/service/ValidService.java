package com.example.board.service;

import org.springframework.validation.BindingResult;

import java.util.Map;

public interface ValidService {

    Map<String, String> validateHandling(BindingResult bindingResult);
}
