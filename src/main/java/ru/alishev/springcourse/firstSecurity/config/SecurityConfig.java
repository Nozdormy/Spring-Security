package ru.alishev.springcourse.firstSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.alishev.springcourse.firstSecurity.services.PersonDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    //Конфигурируем авторизацию
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Используем свое представление ввода логина пароля
        http.csrf().disable()     // Отключение защиты от межсайтовой подделки запросов
                .authorizeRequests()                      // все запросы проходят через авторизацию
                .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()//Пускаем всех без аутетификации
                .anyRequest().authenticated()         //Для всем других запросов должен быть аутентифицирован
                .and()                                //Переход к настройке страницы логина
                .formLogin().loginPage("/auth/login") //Адрес страницы с логином
                .loginProcessingUrl("/process_login") //Адрес куда пойдут данные с формы
                .defaultSuccessUrl("/hello", true) //Куда переправить после удачной аутентификации
                .failureUrl("/auth/login?error") //Куда после неудачной
                .and()
                .logout().logoutUrl("/logout") //При переходе на этот адрес стирает cookie и удаляет session (Выход из учетной записи)
                .logoutSuccessUrl("/auth/login");// Куда переходит при успешном выходе
    }

    // Настраиваем аунтификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService);
    }

    // Как шифруется пароль
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        // Пароль никак не шифруется
        return NoOpPasswordEncoder.getInstance();
    }
}
