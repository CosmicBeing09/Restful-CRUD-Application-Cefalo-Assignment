package com.crud.service;

import com.crud.jwt.JwtTokenUtil;
import com.crud.jwt.models.JwtRequest;
import com.crud.model.User;
import com.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public Boolean createUser(User user){
        Optional<User> userOptional = userRepository.findById(user.getUserId());
        return userOptional.map(t -> false)
                .orElseGet(() -> {
                    String hashPass = passwordEncoder.encode(user.getPassword());
                    user.setPassword(hashPass);
                    userRepository.save(user);
                    return true;
                });
    }

    public String authenticate(JwtRequest request) throws Exception {
       check(request.getUserId(), request.getPassword());

        final UserDetails userDetails = loadUserByUsername(request.getUserId());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    private void check(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User temp = userRepository.getOne(userId);

        return new org.springframework.security.core.userdetails.User(temp.getUserId(),temp.getPassword(),new ArrayList<>());
    }
}

