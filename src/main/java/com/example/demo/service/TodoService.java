package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<TodoEntity> update(final TodoEntity entity) {
        //(1) 저장할 엔터티가 유효한지 확인. Create Todo에서 구현함
        validate(entity);

        //(2) 넘겨받은 엔터티 id를 이용해 DB에 있는 TodoEntity를 가져온다. 존재하지 않는 엔터티는 업데이트 X
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        // original 변수에 TodoEntity 객체가 담겨있다면... (람다식)
        original.ifPresent(todo -> {
            //(3) 반환된 TodoEntity가 존재하면 값을 새 인자 entity 값으로 덮어 씌운다.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            //(4) 데이터베이스에 새 값을 저장한다.
            repository.save(todo);
        });

        // retrieve 검색으로 사용자의 모든 Todo 리스트 리턴
        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity) {
        //(1) 저장할 엔터티가 유효한지 확인 (Create Todo에서 구현함)
        validate(entity);

        try {
            repository.delete(entity);  // (2) 엔터티를 삭제한다.
        } catch(Exception e) {
            //(3) exception 발생 시 id와 exception 로깅
            log.error("error deleting entity ", entity.getId(), e);
            //(4)컨트롤러로 exception 보냄. db 내부 로직을 캡슐화하려면 새 exception 오브젝트 리턴
            throw new RuntimeException("error deleting entity " + entity.getId());
        }
        return retrieve(entity.getUserId());    // (5) 새 Todo 리스트를 검색해서 가져와 리턴
    }

    private void validate(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
