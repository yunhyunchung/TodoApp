package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @PostMapping    // TodoItem 생성 & Todo리스트 리턴
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";  // 임시 아이디
            //(1) TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            // (2) id를 null로 초기화한다. 생성 당시에는 id가 없어야 하기 때문.
            entity.setId(null);

            //(3) 임시 사용자 아이디 설정. 현재는 한 사용자만 로그인 없이 사용할 수 있는 앱이다.
            entity.setUserId(temporaryUserId);

            //(4) 서비스를 이용해 TodoEntity 생성
            List<TodoEntity> entities = service.create(entity);

            //(5) 자바 스트림을 이용해 리턴된 entity 리스트 => TodoDTO 리스트로 변환한다. (service로 전달)
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());  // map은 mapping 함수 (원소 변환. TodoEntity들이 TodoDTO 생성자로 전달됨)

            //(6) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            //(7) ResponseDTO 리턴
            return ResponseEntity.ok().body(response);

        } catch(Exception e) {
            //(8) 혹시 예외가 있는 경우 dto 대신 error에 메시지 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        String temporaryUserId = "temporary-user";
        //(1) 서비스 메소드의 retrieve() 메소드를 사용해 Todo 리스트 가져옴
        List<TodoEntity> entities = service.retrieve(temporaryUserId);

        //(2) 자바 스트림을 이용해 리턴된 entity 리스트 => TodoDTO 리스트로 변환한다. (service로 전달)
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        //(3) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        //(4) ResponseDTO 리턴
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {     // TodoItem 수정
        String temporaryUserId = "temporary-user";

        TodoEntity entity = TodoDTO.toEntity(dto);      //(1) dto를 entity로 변환
        entity.setUserId(temporaryUserId);      //(2) id를 temporaryUserId로 초기화.

        // (3) 서비스를 이용해 entity 업데이트
        List<TodoEntity> entities = service.update(entity);

        // (4), (5), (6) 반복 (entity => todoDto => responseDTO)
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());  // (4) 자바 스트림을 이용해 리턴된 엔터티 리스트 => TodoDTO 리스트로 변환
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();  // (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
        return ResponseEntity.ok().body(response);  // (6) ResponseDTO 리턴
    }

    @DeleteMapping          // TodoItem 삭제
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";

            TodoEntity entity = TodoDTO.toEntity(dto);   //(1) DTO를 TodoEntity로 변환
            entity.setUserId(temporaryUserId);      //(2) 임시 사용자 아이디 설정

            //(3) 서비스를 이용해 entity 삭제
            List<TodoEntity> entities = service.delete(entity);

            // (4), (5), (6) 반복 (entity => todoDto => responseDTO)
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // (7) 혹시 예외가 있는 경우 dto 대신 error에 메시지 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
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
