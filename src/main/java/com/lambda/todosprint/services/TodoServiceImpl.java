package com.lambda.todosprint.services;

import com.lambda.todosprint.daos.TodoDao;
import com.lambda.todosprint.models.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service(value="todoService")
public class TodoServiceImpl implements TodoService
{
    @Autowired
    TodoDao todoDao;

    @Override
    public Todo findById(Long id)
    {
        return todoDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));
    }

    @Override
    @Transactional
    public Todo update(Todo todo)
    {
        return todoDao.save(todo);
    }

    @Override
    @Transactional
    public Todo save(Todo todo)
    {
        return todoDao.save(todo);
    }

    @Override
    public void delete(Long id)
    {

    }
}
