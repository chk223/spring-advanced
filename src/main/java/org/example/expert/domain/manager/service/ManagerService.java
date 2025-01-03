package org.example.expert.domain.manager.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.ApiException;
import org.example.expert.domain.common.exception.util.ErrorMessage;
import org.example.expert.domain.common.exception.util.ExceptionGenerator;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public ManagerSaveResponse saveManager(AuthUser authUser, long todoId, ManagerSaveRequest managerSaveRequest) {
        // 일정을 만든 유저
        User user = User.fromAuthUser(authUser);
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.TODO_NOT_FOUND, ApiException.class));

        if (!ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.DIFFERENT_USER, ApiException.class);
        }

        User managerUser = userRepository.findById(managerSaveRequest.getManagerUserId())
                .orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.USER_NOT_FOUND, ApiException.class));

        if (ObjectUtils.nullSafeEquals(user.getId(), managerUser.getId())) {
            throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.SELF_MANAGER_IS_NOT_ALLOWED, ApiException.class);
        }

        Manager newManagerUser = new Manager(managerUser, todo);
        Manager savedManagerUser = managerRepository.save(newManagerUser);

        return new ManagerSaveResponse(
                savedManagerUser.getId(),
                new UserResponse(managerUser.getId(), managerUser.getEmail())
        );
    }

    public List<ManagerResponse> getManagers(long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() ->ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.TODO_NOT_FOUND, ApiException.class));

        List<Manager> managerList = managerRepository.findAllByTodoId(todo.getId());

        List<ManagerResponse> dtoList = new ArrayList<>();
        for (Manager manager : managerList) {
            User user = manager.getUser();
            dtoList.add(new ManagerResponse(
                    manager.getId(),
                    new UserResponse(user.getId(), user.getEmail())
            ));
        }
        return dtoList;
    }

    @Transactional
    public void deleteManager(long userId, long todoId, long managerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.USER_NOT_FOUND, ApiException.class));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.TODO_NOT_FOUND, ApiException.class));

        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.INVALID_USER_INFO, ApiException.class);
        }

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.MANAGER_NOT_FOUND, ApiException.class));

        if (!ObjectUtils.nullSafeEquals(todo.getId(), manager.getTodo().getId())) {
            throw ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.USER_NOT_MATCH, ApiException.class);
        }

        managerRepository.delete(manager);
    }
}
