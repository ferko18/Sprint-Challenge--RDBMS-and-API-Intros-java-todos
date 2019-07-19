package com.lambda.todosprint.services;

import com.lambda.todosprint.models.Todo;

public interface TodoService
{
    Todo save(Todo todo);
    Todo findById(Long id);
    Todo update(Todo todo);
    void delete(Long id);
}
