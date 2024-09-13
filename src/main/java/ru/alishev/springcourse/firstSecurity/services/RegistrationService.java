package ru.alishev.springcourse.firstSecurity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.springcourse.firstSecurity.models.Person;
import ru.alishev.springcourse.firstSecurity.repositories.PeopleRepository;

@Service
public class RegistrationService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
        //Шифруем пароль
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        //Записываем в БД зашифрованный пароль
        person.setPassword(encodedPassword);

        peopleRepository.save(person);
    }
}
