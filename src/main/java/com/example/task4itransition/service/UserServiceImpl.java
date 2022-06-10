package com.example.task4itransition.service;

import com.example.task4itransition.entity.User;
import com.example.task4itransition.entity.UserPrincipal;
import com.example.task4itransition.exception.EmailExistException;
import com.example.task4itransition.exception.UserNotFoundException;
import com.example.task4itransition.exception.UsernameExistException;
import com.example.task4itransition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("NO_USER_FOUND_BY_USERNAME" + username);
        } else{
            user.setLast_login_time(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            return userPrincipal;
        }
    }

    @Override
    public User register(User user){
        user.setRegistration_time(new Date());
        user.setPassword(encodePassword(user.getPassword()));
        user.setStatus(true);
        userRepository.save(user);
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByUsername(email);
    }

    @Override
    public void blockUser(List<Long> users) {
        for(int i=0; i<users.size(); i++){
            Optional<User> user = userRepository.findById(users.get(i));
            user.get().setStatus(false);
            userRepository.save(user.get());
        }
    }

    @Override
    public void activateUser(List<Long> users) {
        for(int i=0; i<users.size(); i++){
            Optional<User> user = userRepository.findById(users.get(i));
            user.get().setStatus(true);
            userRepository.save(user.get());
        }
    }

    @Override
    public void deleteUser(List<Long> users) {
        for(int i=0; i<users.size(); i++){
            Optional<User> user = userRepository.findById(users.get(i));
            userRepository.delete(user.get());
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean checkUniquenessUsername(String username) {
        Optional<User> xdd = Optional.ofNullable(findUserByUsername(username));
        if (xdd.isPresent()) {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean checkUniquenessEmail(String email) {
        Optional<User> xdd = Optional.ofNullable(findUserByEmail(email));
        if (xdd.isPresent()) {
            return true;
        }
        else
        {
            return false;
        }
    }

    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

}
