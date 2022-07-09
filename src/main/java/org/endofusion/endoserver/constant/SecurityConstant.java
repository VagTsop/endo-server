package org.endofusion.endoserver.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000;//5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer"; // 'Bearer' in front of the token means whoever gives me that token i dont have to do any further verification
    public static final String JWT_TOKEN_HEADER = "Jwt-Token"; // this is our custom header
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token Cannot be verified"; // whenever im trying to decypher the token if i cant that means the token has been tampered withn and we will show that message
    public static final String ENDOFUSION_MANAGEMENT_PLATFORM ="Endofusion Management Platform"; // whenever the token is issued, once you try to decypher the token you will see all of this information. The company which issued the token
    public static final String ENDOFUSION_MANAGEMENT_PLATFORM_ADMINISTRATION = "User Management Portal"; // who will be using that token - the audience
    public static final String AUTHORITIES = "authorities"; // the authorities that im gonna pass in to the user
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page"; //  Message to send the user if they have to provide a token but they dont - are forbidden to access a specific resource
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS"; // if the request comes in to check to see what the server will accept or not
    public static final String[] PUBLIC_URLS = {"user/login", "user/register", "user/resetpassword/**", "/user/image/**"}; // All the urls that im going to be allowed to be accessed without security
}
