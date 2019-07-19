package com.lambda.todosprint;

import com.lambda.todosprint.daos.RoleDao;
import com.lambda.todosprint.daos.UserDao;
import com.lambda.todosprint.models.Role;
import com.lambda.todosprint.models.Todo;
import com.lambda.todosprint.models.User;
import com.lambda.todosprint.models.UserRoles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@Transactional
@Component
public class SeedData implements CommandLineRunner
{
    RoleDao rolerepos;
    UserDao userrepos;

    public SeedData(RoleDao rolerepos, UserDao userrepos)
    {
        this.rolerepos = rolerepos;
        this.userrepos = userrepos;
    }

    @Override
    public void run(String[] args) throws Exception
    {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(), r1));
        admins.add(new UserRoles(new User(), r2));

        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(), r2));

        rolerepos.save(r1);
        rolerepos.save(r2);

        User u1 = new User("erko", "erko", users);
        User u2 = new User("admin", "password", admins);

        ArrayList<Todo> todos = new ArrayList<>();
        Todo t1 = new Todo("pass sprint challenge", "7/19/2019", false, u1);
        Todo t2 = new Todo("learn java ","6/4/2019",false, u1);
        todos.addAll(Arrays.asList(t1, t2));

        u1.setTodos(todos);

        userrepos.save(u1);
        userrepos.save(u2);
    }
}
