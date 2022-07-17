package org.endofusion.endoserver.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000; // 5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String ENDOFUSION_MANAGEMENT_PLATFORM ="Endofusion Management Platform"; // whenever the token is issued, once you try to decypher the token you will see all of this information. The company which issued the token
    public static final String ENDOFUSION_MANAGEMENT_PLATFORM_ADMINISTRATION = "User Management Portal"; // who will be using that token - the audience
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = { "/api/user/login", "/api/user/register", "/api/user/image/**" };
    //public static final String[] PUBLIC_URLS = { "**" };

}
