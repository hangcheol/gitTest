package com.example.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidServiceImpl implements ValidService{

    @Override
    public Map<String, String> validateHandling(BindingResult bindingResult) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError err : bindingResult.getFieldErrors()) {
            validatorResult.put(err.getField(), err.getDefaultMessage());
        }
        return validatorResult;
    }
}
