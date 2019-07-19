package com.lambda.todosprint.Controllers;

import com.lambda.todosprint.models.Todo;
import com.lambda.todosprint.models.User;
import com.lambda.todosprint.services.TodoService;
import com.lambda.todosprint.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class controller
{
    @Autowired
    UserService userService;
    @Autowired
    TodoService todoService;

    @GetMapping("/home")
    public ResponseEntity<?> doTest()
    {
        return new ResponseEntity<>("welcome to home page", HttpStatus.OK);
    }

    //  1. GET /users/mine - return the user and todo based off of the authenticated user. You can only look up your own.
    @GetMapping(value="/users/mine", produces = "application/json")
    public ResponseEntity<?> getUserAndTodos()
    {
        String uname;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
        {
            return new ResponseEntity<>("not authorized", HttpStatus.FORBIDDEN);
        }
        uname = authentication.getName();

        // use name to get user object
        User user = userService.findUserByUsername(uname);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }

        return new ResponseEntity<>(user, OK);
    }

    // 2. POST /users - adds a user. Can only be done by an admin.
    @PostMapping(value="/users", consumes="application/json", produces="application/json")
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        User rtn = userService.save(user);
        return new ResponseEntity<>(rtn, OK);
    }

    // 3. POST /users/todo/{userid} - adds a todo to the assigned user. Can be done by any user.
    @PostMapping(value="/users/todo/{userid}", consumes="application/json", produces="application/json")
    public ResponseEntity<?> addTodoToUser(@PathVariable long userid, @Valid @RequestBody Todo todo)
    {
        return new ResponseEntity<>(userService.updateTodos(todo, userid), OK);
    }

    // 4. PUT /todos/todoid/{todoid} - updates a todo based on todoid. Can be done by any user.
    @PutMapping(value="/todos/todoid/{todoid}", consumes="application/json", produces="application/json")
    public ResponseEntity<?> updateTodo(@PathVariable long todoid, @Valid @RequestBody Todo todo)
    {
        Todo toUpdate = todoService.findById(todoid);
        if (todo.getDesc() != null) {
            toUpdate.setDesc(todo.getDesc());
        }
        return new ResponseEntity<>(todoService.update(toUpdate), OK);
    }

    //  5. DELETE /users/userid/{userid} - Deletes a user based off of their userid and deletes all their associated todos. Can only be done by an admin.
    @DeleteMapping(value="/users/userid/{userid}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userid)
    {
        userService.delete(userid);
        return new ResponseEntity<>("Successfully deleted user with id: " + userid, OK);
    }

    // 6. get all users
    @GetMapping("/allusers")
    public ResponseEntity<?> getAllUsers()
    {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, OK);
    }
}
