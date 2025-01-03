package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.exception.ApiException;
import org.example.expert.domain.common.exception.util.ErrorMessage;
import org.example.expert.domain.common.exception.util.ExceptionGenerator;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "managers")
public class Manager {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 일정 만든 사람 id
    private User user;
    @ManyToOne(fetch = FetchType.LAZY) // 일정 id
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    public Manager(User user, Todo todo) {
        this.user = user;
        this.todo = todo;
    }

    public ManagerSaveResponse toSaveResponse(User managerUser) {
        return new ManagerSaveResponse(id,
        new UserResponse(managerUser.getId(), managerUser.getEmail()));
    }
    public ManagerResponse toResponse(User managerUser) {
        return new ManagerResponse(id,
                new UserResponse(managerUser.getId(), managerUser.getEmail()));
    }

    public void isMyToDoOrThrow(Todo todo) {
        if(!this.todo.getId().equals(todo.getId())) throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.USER_NOT_MATCH, ApiException.class);
    }
}
