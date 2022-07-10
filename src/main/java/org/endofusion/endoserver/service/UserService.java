package org.endofusion.endoserver.service;

import org.endofusion.endoserver.domain.User;
import org.endofusion.endoserver.exception.domain.EmailExistException;
import org.endofusion.endoserver.exception.domain.UserNotFoundException;
import org.endofusion.endoserver.exception.domain.UsernameExistException;

import java.util.List;

public interface UserService {
    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
