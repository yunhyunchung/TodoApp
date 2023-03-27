package com.example.demo.dto;

import com.example.demo.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {
    private String id;  // 오브젝트 아이디
    private String title;  // Todo 타이틀
    private boolean done;

    // DTO는 TodoEntity 객체로부터 속성 값 받아서 생성
    public TodoDTO(final TodoEntity entity) {
        this.id = entity.getId();
        // userId는 숨김
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }

    // 클라이언트에게 받은 TodoDTO -> TodoEntity로 변환 => Service에게 전달
    public static TodoEntity toEntity(TodoDTO dto) {
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .build();
    }
}
