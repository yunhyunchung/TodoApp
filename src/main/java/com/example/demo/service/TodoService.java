package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    @Autowired
    private TodoRepository repository;
    public String testService() {
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();  // TodoEntity 생성
        repository.save(entity);                                                    // TodoEntity 저장
        TodoEntity savedEntity = repository.findById(entity.getId()).get();         // TodoEntity 검색
        return savedEntity.getTitle();
    }
}
