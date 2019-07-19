package com.lambda.todosprint.daos;

import com.lambda.todosprint.models.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoDao extends CrudRepository<Todo, Long>
{

}
