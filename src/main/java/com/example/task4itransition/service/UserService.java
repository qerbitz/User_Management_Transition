package com.example.task4itransition.service;

import com.example.task4itransition.entity.User;
import com.example.task4itransition.exception.EmailExistException;
import com.example.task4itransition.exception.UserNotFoundException;
import com.example.task4itransition.exception.UsernameExistException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    User register(User user) throws UserNotFoundException, UsernameExistException, EmailExistException;

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    void blockUser(List<Long> users);

    void activateUser(List<Long> users);

    void deleteUser(List<Long> users);

    boolean checkUniqueness(String username);

    List<User> getAllUsers();

}
