package ru.astondevs.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * Утилитный класс для операций с JSON Web Token (JWT), таких как создание, проверка и извлечение данных.
 */
public class JwtUtil {
    private final String SECRET_KEY;
    private static Logger log = LoggerFactory.getLogger(JwtUtil.class);


    public JwtUtil(String secretKey) {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new RuntimeException("Секретный ключ не должен быть пустым");
        }
        this.SECRET_KEY = secretKey;
    }

    /**
     * Генерирует JWT токен для заданного пользователя.
     *
     * @param username имя пользователя
     * @return сгенерированный JWT токен
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Создает JWT токен на основе предоставленных утверждений (claims) и субъекта (subject).
     *
     * @param claims утверждения, которые будут включены в JWT токен
     * @param subject субъект, для которого создается JWT токен
     * @return строковое представление JWT токена
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * Проверяет валидность JWT токена для указанного пользователя.
     *
     * @param token    JWT токен
     * @param username имя пользователя
     * @return {@code true} если токен валиден, иначе {@code false}
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        boolean isValid = extractedUsername.equals(username) && !isTokenExpired(token);
        log.info("Validating token for username '{}'. Token valid: {}", username, isValid);
        return isValid;
    }

    /**
     * Извлекает имя пользователя из JWT токена.
     *
     * @param token JWT токен
     * @return имя пользователя
     */
    public String extractUsername(String token) {
        String username = extractClaim(token, Claims::getSubject);
        log.info("Extracted username from token: {}", username);
        return username;
    }

    /**
     * Извлекает дату истечения срока действия JWT токена.
     *
     * @param token JWT токен
     * @return дата истечения срока действия
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает определенные данные из JWT токена с помощью указанного функционального интерфейса.
     *
     * @param token          JWT токен
     * @param claimsResolver функциональный интерфейс для извлечения данных из JWT
     * @return данные, извлеченные из JWT токена
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Извлекает все утверждения (claims) из JWT токена.
     *
     * @param token JWT токен
     * @return объект Claims, содержащий утверждения из JWT токена
     */
    private Claims extractAllClaims(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        log.info("Extracted claims from token: {}", claims);
        return claims;
    }

    /**
     * Проверяет, истек ли срок действия JWT токена.
     *
     * @param token JWT токен
     * @return {@code true}, если срок действия токена истек, иначе {@code false}
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}