package ru.alishev.springcourse.firstSecurity.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}") //Откуда берется значение для поля
    private String secret;

    //Создание токена
    public String generateToken(String username) {
        //Срок годности токена (Время в текущей тайм зоне + 60 минут)
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details") // Что хранится в токене
                .withClaim("username", username)// Лежат пары ключ : значение
                //.withClaim(); может быть несколько
                .withIssuedAt(new Date()) // Когда был выдан токен
                .withIssuer("Google")//Кто выдал токен лежит название приложения
                .withExpiresAt(expirationDate)//Когда закончится срок годности токена
                .sign(Algorithm.HMAC256(secret));//Выбор алгоритма шифрования и секретная строка для созд токена
    }

    // Валидация и извлечение пар ключ : значение чтобы найти пользователя по базе и провести аутентификацию
    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        //Только токен подходящий под эти параметры может валидироваться
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("Google")
                .build();

        // Раскодированный токен
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
