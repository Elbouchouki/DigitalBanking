package ma.elbouchouki.digitalbanking.security.config;

public class SecurityConstant {

    public static String SECRET = "TQ1MWEtOTM3Ny1kZjQwMzA4MjBlOGIiLCJleHAiOj";
    public static final int ACCESS_EXPIRATION_TIME = 30 * 60 * 1000;
    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 150 * 60 * 1000;
    public static final String REFRESH_PATH = "/api/v1/refresh-token";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String HEADER_STRING = "Authorization";

    public static class SecurityExceptionMessage {
        public static final String INVALID_CREDENTIALS = "businessException.InvalidCredentials.message";
        public static final String INVALID_TOKEN = "businessException.InvalidRefreshToken.message";
    }
}
