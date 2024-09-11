package ru.alishev.springcourse.firstSecurity.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.alishev.springcourse.firstSecurity.models.Person;

import java.util.Collection;

// Обёртка типа класса DAO
public class PersonDetails implements UserDetails {

    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    // Нужно для получения данных для аутентификации
    public Person getPerson() {
        return person;
    }

    // Для авторизации возвращает список прав пользователя(на какие страницы может зайти)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
    }

    // Активна ли учетная запись
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Заблокирована ли учетная запись
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Пароль не просрочен
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Работает ли аккаунт
    @Override
    public boolean isEnabled() {
        return true;
    }
}
