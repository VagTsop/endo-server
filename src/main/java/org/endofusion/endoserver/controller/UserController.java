package org.endofusion.endoserver.controller;
import org.endofusion.endoserver.domain.User;
import org.endofusion.endoserver.exception.domain.EmailExistException;
import org.endofusion.endoserver.exception.domain.ExceptionHandling;
import org.endofusion.endoserver.exception.domain.UserNotFoundException;
import org.endofusion.endoserver.exception.domain.UsernameExistException;
import org.endofusion.endoserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path={"/", "/api/user"})
public class UserController extends ExceptionHandling {
    private  UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
}
