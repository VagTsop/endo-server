package org.endofusion.endoserver.controller;
import org.endofusion.endoserver.exception.domain.ExceptionHandling;
import org.endofusion.endoserver.exception.domain.UserNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path={"/", "/api/user"})
public class UserController extends ExceptionHandling {

    @GetMapping("/home")
    public String showUser() throws UserNotFoundException {
    //    return "application works";
        throw new UserNotFoundException("The user was not found");
    }
}
