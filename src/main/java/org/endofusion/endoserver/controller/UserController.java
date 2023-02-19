package org.endofusion.endoserver.controller;

import org.endofusion.endoserver.domain.HttpResponse;
import org.endofusion.endoserver.domain.User;
import org.endofusion.endoserver.domain.UserPrincipal;
import org.endofusion.endoserver.dto.UserDto;
import org.endofusion.endoserver.exception.domain.*;
import org.endofusion.endoserver.provider.JWTTokenProvider;
import org.endofusion.endoserver.request.UserRequest;
import org.endofusion.endoserver.response.UserResponse;
import org.endofusion.endoserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import static org.endofusion.endoserver.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.*;
import static org.endofusion.endoserver.constant.UserImplConstants.*;

@RestController
@RequestMapping(path = {"/", "/api/user"})
public class UserController extends ExceptionHandling {
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user, HttpServletRequest request) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, IOException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPassword(), getSiteURL(request));
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<HttpResponse> passwordReset(@RequestBody String email, HttpServletRequest request) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, IOException, EmailNotFoundException {
        userService.passwordReset(email, getPasswordResetURL(request));
        return response(OK, RESET_PASSWORD_EMAIL_SENT + email);
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ResponseEntity<HttpResponse> changePassword(@PathParam("code") String code, @RequestBody String password) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, IOException, EmailNotFoundException, TokenNotFoundException {
        String message = userService.changePassword(code,password);
        return response(OK, message);
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        String port = Integer.toString(request.getLocalPort());
        return siteURL.replace("endo-server-service:8080", "127.0.0.1:63788").replace(request.getServletPath(), "/register");
    }

    private String getPasswordResetURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        String port = Integer.toString(request.getLocalPort());
        return siteURL.replace("endo-server-service:8080","127.0.0.1:63788").replace(request.getServletPath(), "/password-reset");
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public ResponseEntity<HttpResponse> verifyUser(@PathParam("code") String code) throws IOException, MessagingException, TokenNotFoundException {
        String message = userService.verify(code);
        return response(OK, message);
    }

    @PostMapping("/resend")
    public ResponseEntity<HttpResponse> resendToken(@PathParam("code") String code, HttpServletRequest request) throws IOException, MessagingException, TokenNotFoundException {
        userService.resend(code, getSiteURL(request));
        return response(OK, RESEND_VERIFICATION_EMAIL);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @RequestMapping("/get-users-list")
    public ResponseEntity<Page<UserDto>> getUsersList(
            Pageable pageable,
            @RequestParam Optional<String> userId,
            @RequestParam Optional<String> username,
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName,
            @RequestParam Optional<String> email,
            @RequestParam Optional<Boolean> status,
            @RequestParam Optional<Boolean> locked
    ) {
        Page<UserDto> retVal = userService.getUsersList(pageable, userId.orElse(null), username.orElse(null), firstName.orElse(null), lastName.orElse(null), email.orElse(null), status.orElse(null), locked.orElse(null));
        return ResponseEntity.status(HttpStatus.OK).body(retVal);
    }

    @GetMapping("/fetch-usernames")
    public ResponseEntity<List<UserDto>> fetchUsernames() {
        List<UserDto> retVal = userService.fetchUsernames();
        return ResponseEntity.status(HttpStatus.OK).body(retVal);
    }

    @GetMapping("/fetch-firstnames")
    public ResponseEntity<List<UserDto>> fetchFirstNames() {
        List<UserDto> retVal = userService.fetchFirstNames();
        return ResponseEntity.status(HttpStatus.OK).body(retVal);
    }

    @GetMapping("/fetch-lastnames")
    public ResponseEntity<List<UserDto>> fetchLastNames() {
        List<UserDto> retVal = userService.fetchLastNames();
        return ResponseEntity.status(HttpStatus.OK).body(retVal);
    }

    @GetMapping("/fetch-emails")
    public ResponseEntity<List<UserDto>> fetchEmails() {
        List<UserDto> retVal = userService.fetchEmails();
        return ResponseEntity.status(HttpStatus.OK).body(retVal);
    }

    @RequestMapping(value = "/create-user", method = RequestMethod.POST)
    public ResponseEntity<Long> createUser(@RequestBody UserRequest userRequest, HttpServletRequest request) throws UserNotFoundException, UsernameExistException, MessagingException, EmailExistException, IOException {
        UserDto userDto = new UserDto(userRequest, null, false);
        return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(userDto,getSiteURL(request)));
    }

    @PutMapping("/update-user")
    public ResponseEntity<Boolean> updateUser(@RequestParam long id, @RequestBody UserRequest request) {
        UserDto userDto = new UserDto(request, id, true);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto));
    }

    @GetMapping("/get-user-by-id")
    public ResponseEntity<UserResponse> getUserById(@RequestParam Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new UserResponse(userDto));
    }

    @RequestMapping("/delete-user")
    public ResponseEntity<Boolean> deleteUser(@RequestBody Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }
}
