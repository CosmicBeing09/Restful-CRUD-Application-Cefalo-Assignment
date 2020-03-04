package com.crud.service;

import com.crud.model.User;
import com.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder){
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }


    public ResponseEntity createUser(User user){
        Optional<User> userOptional = userRepository.findById(user.getUserId());
        return userOptional.map(t -> ResponseEntity.badRequest().body("Username already taken"))
                .orElseGet(() -> {
                    String hashPass = passwordEncoder.encode(user.getPassword());
                    user.setPassword(hashPass);
                    userRepository.save(user);
                    return new ResponseEntity<String>("Registration Successful", HttpStatus.CREATED);
                });



    }


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User temp = userRepository.getOne(userId);
        return new org.springframework.security.core.userdetails.User(temp.getUserId(),temp.getPassword(),new ArrayList<>());
    }
}

