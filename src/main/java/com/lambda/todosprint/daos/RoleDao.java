package com.lambda.todosprint.daos;

import com.lambda.todosprint.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleDao extends CrudRepository <Role, Long>
{
}
