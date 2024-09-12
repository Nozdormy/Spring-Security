package ru.alishev.springcourse.firstSecurity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alishev.springcourse.firstSecurity.models.Person;
import ru.alishev.springcourse.firstSecurity.repositories.PeopleRepository;

import java.util.Optional;

@Service
public class PersonService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Optional<Person> loadUserByUsername(String s) {
        Optional<Person> person = peopleRepository.findPersonByUsername(s);
        return person;
    }
}
