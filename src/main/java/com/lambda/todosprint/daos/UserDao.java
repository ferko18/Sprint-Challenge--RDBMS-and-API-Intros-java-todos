package com.lambda.todosprint.daos;

import com.lambda.todosprint.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long>
{
    User findByUsername(String username);
}
