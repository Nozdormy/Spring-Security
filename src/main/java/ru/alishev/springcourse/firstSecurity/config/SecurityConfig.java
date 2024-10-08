package ru.alishev.springcourse.firstSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.alishev.springcourse.firstSecurity.services.PersonDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Включает @PreAuthorize
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonDetailsService personDetailsService;
    private final JWTFilter jwtFilter;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter jwtFilter) {
        this.personDetailsService = personDetailsService;
        this.jwtFilter = jwtFilter;
    }

    //Конфигурируем авторизацию
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Используем свое представление ввода логина пароля
        http
                .csrf().disable()// Отключили защиту от межсайтовой подделки запросов
                .authorizeRequests()                      // все запросы проходят через авторизацию
                .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()//Пускаем всех без аутетификации
                .anyRequest().hasAnyRole("USER", "ADMIN") //Для всех других запросов эти роли
                .and()                                //Переход к настройке страницы логина
                .formLogin().loginPage("/auth/login") //Адрес страницы с логином
                .loginProcessingUrl("/process_login") //Адрес куда пойдут данные с формы
                .defaultSuccessUrl("/hello", true) //Куда переправить после удачной аутентификации
                .failureUrl("/auth/login?error") //Куда после неудачной
                .and()
                .logout().logoutUrl("/logout") //При переходе на этот адрес стирает cookie и удаляет session (Выход из учетной записи)
                .logoutSuccessUrl("/auth/login") // Куда переходит при успешном выходе
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);// Сессия больше не сохраняется на нашем сервере

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }

    // Настраиваем аунтификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    // Как шифруется пароль
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
