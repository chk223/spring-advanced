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
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        todo.userIsWriterOrThrow(user);
        User managerUser = userRepository.findById(managerSaveRequest.getManagerUserId())
                .orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.USER_NOT_FOUND, ApiException.class));

        user.isSameUserThenThrow(managerUser);
        Manager newManagerUser = new Manager(managerUser, todo);
        Manager savedManagerUser = managerRepository.save(newManagerUser);

        return savedManagerUser.toSaveResponse(managerUser);
    }

    public List<ManagerResponse> getManagers(long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() ->ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.TODO_NOT_FOUND, ApiException.class));

        List<Manager> managerList = managerRepository.findAllByTodoId(todo.getId());

        List<ManagerResponse> dtoList = new ArrayList<>();
        for (Manager manager : managerList) {
            User user = manager.getUser();
            dtoList.add(manager.toResponse(user));
        }
        return dtoList;
    }

    @Transactional
    public void deleteManager(long userId, long todoId, long managerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.USER_NOT_FOUND, ApiException.class));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.TODO_NOT_FOUND, ApiException.class));

        todo.userIsWriterOrThrow(user);

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> ExceptionGenerator.generateExceptionOrThrow(ErrorMessage.MANAGER_NOT_FOUND, ApiException.class));

        manager.isMyToDoOrThrow(todo);

        managerRepository.delete(manager);
    }
}
