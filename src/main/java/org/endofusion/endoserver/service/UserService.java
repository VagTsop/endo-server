package org.endofusion.endoserver.service;

import org.endofusion.endoserver.domain.User;
import org.endofusion.endoserver.dto.UserDto;
import org.endofusion.endoserver.exception.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {
    User register(String firstName, String lastName, String username, String email, String password, String siteURL) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, IOException;

    void passwordReset(String email, String siteURL) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, IOException, EmailNotFoundException;

    String changePassword(String code, String password) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, IOException, EmailNotFoundException, TokenNotFoundException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    void deleteUser(String username) throws IOException;

    void resetPassword(String email) throws MessagingException, EmailNotFoundException, IOException;

    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    String verify(String verificationCode) throws IOException, MessagingException, TokenNotFoundException;

    boolean resend(String code, String verificationCode) throws IOException, MessagingException, TokenNotFoundException;

    ////
    Page<UserDto> getUsersList(Pageable pageable, String userId, String username, String firstName, String lastName, String email, Boolean status, Boolean locked);

    List<UserDto> fetchUsernames();

    List<UserDto> fetchFirstNames();

    List<UserDto> fetchLastNames();

    List<UserDto> fetchEmails();

    long createUser(UserDto dto, String siteURL) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, MessagingException;

    boolean updateUser(UserDto dto);

    UserDto getUserById(long id);

    boolean deleteUser(Long id);
}
