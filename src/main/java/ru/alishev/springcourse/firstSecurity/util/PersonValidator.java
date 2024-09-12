package ru.alishev.springcourse.firstSecurity.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alishev.springcourse.firstSecurity.models.Person;
import ru.alishev.springcourse.firstSecurity.services.PersonService;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        Optional<Person> existPerson = personService.loadUserByUsername(person.getUsername()); // Проверка существует ли такой человек
        if (existPerson.isPresent()) {
            errors.rejectValue("username", "", "Человек с таким именем существует");
        }
    }
}
