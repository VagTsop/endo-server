package org.endofusion.endoserver.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.endofusion.endoserver.constant.EmailStatus;
import org.endofusion.endoserver.domain.User;
import org.endofusion.endoserver.domain.UserPrincipal;
import org.endofusion.endoserver.domain.token.ConfirmationToken;
import org.endofusion.endoserver.dto.UserDto;
import org.endofusion.endoserver.enumeration.Role;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.endofusion.endoserver.constant.FileConstant.*;
import static org.endofusion.endoserver.constant.UserImplConstants.*;
import static org.endofusion.endoserver.enumeration.Role.ROLE_USER;


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

    @Override
    public User register(String firstName, String lastName, String username, String email, String siteURL) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, IOException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
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
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        String randomCode = UUID.randomUUID().toString();
        userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                randomCode,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(20),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        emailService.sendVerificationEmail(user, siteURL, confirmationToken);
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
            confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(20));

            confirmationTokenService.saveConfirmationToken(confirmationToken);

            emailService.sendVerificationEmail(confirmationToken.getUser(), siteURL, confirmationToken);
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
        emailService.sendNewPasswordEmail(confirmationToken.getUser().getFirstName(), confirmationToken.getUser().getPassword(), confirmationToken.getUser().getEmail());
        return EmailStatus.VALID;
    }

    private void enableUser(String email) {
        userRepository.enableAppUser(email);
    }

    @Override
    public User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        String password = generatePassword();
        user.setUserId(generateUserId());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setJoinDate(new Date());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setActive(isActive);
        user.setNotLocked(isNonLocked);
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        userRepository.save(user);
        saveProfileImage(user, profileImage);
        LOGGER.info("New user password: " + password);
        return user;
    }

    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
        User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
        assert currentUser != null;
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNonLocked);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);
        saveProfileImage(currentUser, profileImage);
        return currentUser;
    }

    @Override
    public void resetPassword(String email) throws MessagingException, EmailNotFoundException, IOException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        emailService.sendNewPasswordEmail(user.getFirstName(), password, user.getEmail());
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
        User user = validateNewUsernameAndEmail(username, null, null);
        saveProfileImage(user, profileImage);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void deleteUser(String username) throws IOException {
        User user = userRepository.findUserByUsername(username);
        Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
        FileUtils.deleteDirectory(new File(userFolder.toString()));
        userRepository.deleteById(user.getId());
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException {
        if (profileImage != null) {
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
                + username + DOT + JPG_EXTENSION).toUriString();
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
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
                                      String lastName, String email, Boolean status) {
        return userRepository.getUsersList(pageable, new UserDto(userId, username, firstName, lastName, email, status));
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

        validateNewUsernameAndEmail(EMPTY, dto.getUsername(),  dto.getEmail());
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setJoinDate(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(dto.getStatus());
        user.setNotLocked(dto.getLocked());
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImageUrl(dto.getUsername()));
        user.setProfileImage(dto.getProfileImage());

        String randomCode = UUID.randomUUID().toString();
        userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                randomCode,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(20),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        emailService.sendVerificationEmail(user, siteURL, confirmationToken);
        LOGGER.info("New user password: " + password);

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
