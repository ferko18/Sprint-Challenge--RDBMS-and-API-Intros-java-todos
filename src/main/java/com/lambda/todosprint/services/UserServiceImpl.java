package com.lambda.todosprint.services;

import com.lambda.todosprint.daos.RoleDao;
import com.lambda.todosprint.daos.UserDao;
import com.lambda.todosprint.models.Todo;
import com.lambda.todosprint.models.User;
import com.lambda.todosprint.models.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService
{

    @Autowired
    private UserDao userrepos;

    @Autowired
    private RoleDao rolerepos;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userrepos.findByUsername(username);
        if (user == null)
        {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthority());
    }

    public User findUserById(long id) throws EntityNotFoundException
    {
        return userrepos.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));
    }

    public User findUserByUsername(String username)
    {
        User user = userrepos.findByUsername(username);
        if (user == null)   throw new UsernameNotFoundException("user not found");;
        return user;
    }

    public List<User> findAll()
    {
        List<User> list = new ArrayList<>();
        userrepos.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public void delete(long id)
    {
        if (userrepos.findById(id).isPresent())
        {
            userrepos.deleteById(id);
        }
        else
        {
            throw new EntityNotFoundException(Long.toString(id));
        }
    }

    @Transactional
    @Override
    public User save(User user)
    {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());

        ArrayList<UserRoles> newRoles = new ArrayList<>();
        for (UserRoles ur : user.getUserRoles())
        {
            newRoles.add(new UserRoles(newUser, ur.getRole()));
        }

        // if nothing else set as user
        if (newRoles.size() < 1) {
            newRoles.add(new UserRoles(newUser, rolerepos.findByName("user")));
        }

        newUser.setUserRoles(newRoles);

        return userrepos.save(newUser);
    }

    @Transactional
    @Override
    public User update(User user, long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userrepos.findByUsername(authentication.getName());

        if (currentUser != null)
        {
            if (id == currentUser.getUserid())
            {
                if (user.getUsername() != null)
                {
                    currentUser.setUsername(user.getUsername());
                }

                if (user.getPassword() != null)
                {
                    currentUser.setPassword(user.getPassword());
                }

                if (user.getUserRoles().size() > 0)
                {

                    rolerepos.deleteUserRolesByUserId(currentUser.getUserid());


                    for (UserRoles ur : user.getUserRoles())
                    {
                        rolerepos.insertUserRoles(id, ur.getRole().getRoleid());
                    }
                }

                return userrepos.save(currentUser);
            }
            else
            {
                throw new EntityNotFoundException(Long.toString(id) + " Not current user");
            }
        }
        else
        {
            throw new EntityNotFoundException(authentication.getName());
        }

    }

    @Transactional
    @Override
    public User updateTodos(Todo todo, long id)
    {
        // get user
        User user = userrepos.findById(id).get();

        // set user
        todo.setUser(user);

        // add list to todos
        ArrayList<Todo> todos = new ArrayList<>();
        user.getTodos().iterator().forEachRemaining(todos::add);
        todos.add(todo);

        // set new todo list to user
        user.setTodos(todos);

        // save user
        return userrepos.save(user);
    }
}


