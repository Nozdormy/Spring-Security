package ru.alishev.springcourse.firstSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.firstSecurity.services.PersonDetailsService;

import java.util.Collections;

// Логика аутентификации
@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthProviderImpl(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    // Сравниваем пароль из формы с с паролем в таблице
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        UserDetails personDetails = personDetailsService.loadUserByUsername(username);
        String password = authentication.getCredentials().toString();

        if (!password.equals(personDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        // возвращает (Principal, пароль, список прав)
        return new UsernamePasswordAuthenticationToken(personDetails, password, Collections.emptyList());
    }

    // Логика для какого AuthenticationProvide работает если их несколько
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
