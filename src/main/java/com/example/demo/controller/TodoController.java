package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("todo")
public class TodoController {
    @Autowired
    private TodoService service;    // 생성한 서비스 객체 자동 주입

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService();

        List<String> list = new ArrayList<>();
        list.add(str);

        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.ok().body(response);
    }

    /* 과제
    @GetMapping("testTodo")
    public ResponseEntity<?> testTodo() {
        // 1) Entity 생성
        TodoEntity todoEntity = TodoEntity.builder()
                .id("123")
                .userId("yhchung")
                .title("정윤현")
                .done(true)
                .build();
        // 2) 생성한 Entity를 DTO로 생성 & 변환 => 클라이언트에게 반환함
        TodoDTO response = new TodoDTO(todoEntity);
        return ResponseEntity.ok().body(response);  // status 200
    }
     */
}
