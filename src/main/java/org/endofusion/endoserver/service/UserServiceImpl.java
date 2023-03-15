package org.endofusion.endoserver.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.endofusion.endoserver.constant.EmailStatus;
import org.endofusion.endoserver.domain.User;
import org.endofusion.endoserver.domain.UserPrincipal;
import org.endofusion.endoserver.domain.token.ConfirmationToken;
import org.endofusion.endoserver.dto.UserDto;
import org.endofusion.endoserver.exception.domain.*;
import org.endofusion.endoserver.repository.UserRepository;
import org.endofusion.endoserver.service.token.ConfirmationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static org.endofusion.endoserver.constant.UserImplConstants.*;
import static org.endofusion.endoserver.enumeration.Role.*;


@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService, EmailService emailService, ConfirmationTokenService confirmationTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    public void passwordReset(String email, String siteURL) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, IOException, EmailNotFoundException {

        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }

        String randomCode = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                randomCode,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        emailService.sendEmail(user, siteURL, confirmationToken,4, null);
    }

    @Override
    public String changePassword(String code, String passsword) throws TokenNotFoundException {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(code)
                .orElseThrow(() ->
                        new TokenNotFoundException(TOKEN_NOT_FOUND));

        if (confirmationToken.getConfirmedAt() != null) {
            return EmailStatus.ALREADY_CONFIRMED;
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return EmailStatus.EXPIRED;
        }

        confirmationTokenService.setConfirmedAt(code);
        User newPassword = confirmationToken.getUser();
        newPassword.setPassword(encodePassword(passsword));
        return "Password Changed Successfully";
    }

        @Override
    public User register(String firstName, String lastName, String username, String email, String password, String siteURL) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, IOException {
        validateNewUsernameAndEmail("", username, email);
        User user = new User();
        user.setUserId(generateUserId());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(false);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        String randomCode = UUID.randomUUID().toString();
        userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                randomCode,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        emailService.sendEmail(user, siteURL, confirmationToken, 1, null);
        LOGGER.info("New user password: " + password);
        return user;
    }

    public boolean resend(String verificationCode, String siteURL) throws TokenNotFoundException, IOException, MessagingException {
        if (verificationCode != null) {
            ConfirmationToken confirmationToken = confirmationTokenService
                    .getToken(verificationCode)
                    .orElseThrow(() ->
                            new TokenNotFoundException(TOKEN_NOT_FOUND));

            confirmationToken.setToken(UUID.randomUUID().toString());
            confirmationToken.setCreatedAt(LocalDateTime.now());
            confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(1));

            confirmationTokenService.saveConfirmationToken(confirmationToken);

            emailService.sendEmail(confirmationToken.getUser(), siteURL, confirmationToken, 1,null);
        }

        return true;
    }

        public String verify(String verificationCode) throws IOException, MessagingException, TokenNotFoundException {

        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(verificationCode)
                .orElseThrow(() ->
         new TokenNotFoundException(TOKEN_NOT_FOUND));

        if (confirmationToken.getConfirmedAt() != null) {
            return EmailStatus.ALREADY_CONFIRMED;
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return EmailStatus.EXPIRED;
        }

        confirmationTokenService.setConfirmedAt(verificationCode);

        enableUser(confirmationToken.getUser().getEmail());
        emailService.sendEmail(confirmationToken.getUser(),null,null, 2,null);
        return EmailStatus.VALID;
    }

    private void enableUser(String email) {
        userRepository.enableAppUser(email);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private void validateLoginAttempt(User user) {
        if (user.isNotLocked()) {
            user.setNotLocked(!loginAttemptService.hasExceededMaxAttempts(user.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    @Override
    public Page<UserDto> getUsersList(Pageable pageable, String userId, String username,
                                      String firstName,
                                      String lastName, String email, Boolean status, Boolean locked) {
        return userRepository.getUsersList(pageable, new UserDto(userId, username, firstName, lastName, email, status, locked));
    }

    @Override
    public List<UserDto> fetchFirstNames() {
        return userRepository.fetchFirstNames();
    }

    @Override
    public List<UserDto> fetchLastNames() {
        return userRepository.fetchLastNames();
    }

    @Override
    public List<UserDto> fetchUsernames() {
        return userRepository.fetchUsernames();
    }

    @Override
    public List<UserDto> fetchEmails() {
        return userRepository.fetchEmails();
    }

    @Override
    public long createUser(UserDto dto, String siteURL) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, MessagingException {
        String randomPassword = RandomStringUtils.randomAlphanumeric(10);
        validateNewUsernameAndEmail("", dto.getUsername(),  dto.getEmail());
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setPassword(encodePassword(randomPassword));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setJoinDate(new Date());
        user.setActive(dto.getStatus());
        user.setNotLocked(dto.getLocked());
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        String randomCode = UUID.randomUUID().toString();
        userRepository.save(user);
        ConfirmationToken confirmationToken = new ConfirmationToken(
                randomCode,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(20),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
       emailService.sendEmail(user, siteURL, confirmationToken,3, randomPassword);
        LOGGER.info("New user password: " + randomPassword);
        return user.getId();
    }

    @Override
    public boolean updateUser(UserDto dto) {
        return userRepository.updateUser(dto);
    }

    @Override
    public UserDto getUserById(long id) {
        return userRepository.getUserById(id);
    }
    @Override
    public boolean deleteUser(Long id) {
        return userRepository.deleteUser(id);
    }
}
