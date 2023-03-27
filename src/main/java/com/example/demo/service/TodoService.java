package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
    
    public List<TodoEntity> create(final TodoEntity entity) {
        // 1) Validation 검증
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        
        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }

        // 2) 엔터티 저장 save()
        repository.save(entity);
        log.info("Entity Id : {} is saved", entity.getId());  // 로그인한 id 정보 확인

        // 3) 저장된 엔터티 찾아서 새 TodoList 리턴
        return repository.findByUserId(entity.getUserId());
    }

    // 검색해서 TodoList 리턴
    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }
    
}
