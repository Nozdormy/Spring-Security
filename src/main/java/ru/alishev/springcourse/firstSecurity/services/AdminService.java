package ru.alishev.springcourse.firstSecurity.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SOME_OTHER')") //Или and для обладателей 2 прав
    public void doAdminStuff() {
        System.out.println("Only admin here");
    }
}
