package com.lambda.todosprint.services;

import com.lambda.todosprint.models.Todo;
import com.lambda.todosprint.models.User;

import java.util.List;

public interface UserService
{
    User save(User user);

    List<User> findAll();
    User findUserById(long id);
    User findUserByUsername(String username);

    User update(User user, long id);
    public User updateTodos(Todo todo, long id);

    void delete(long id);

}
