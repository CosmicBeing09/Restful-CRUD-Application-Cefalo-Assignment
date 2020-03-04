package com.crud.controller;

import com.crud.jwt.JwtTokenUtil;
import com.crud.jwt.models.JwtRequest;
import com.crud.jwt.models.JwtResponse;
import com.crud.model.User;
import com.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class UserController {



    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;


//    @Autowired
//    public UserController(AuthenticationManager authenticationManager,JwtTokenUtil jwtTokenUtil,UserService userService){
//        this.authenticationManager = authenticationManager;
//        this.userService = userService;
//        this.jwtTokenUtil = jwtTokenUtil;
//    }

    @PostMapping(value = "/user/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity register(@Valid @RequestBody User user){

        return userService.createUser(user);
    }

    @PostMapping(value = "/user/authenticate")
    public ResponseEntity authenticate(@RequestBody JwtRequest request) throws Exception {

        check(request.getUserId(), request.getPassword());

        final UserDetails userDetails = userService
                .loadUserByUsername(request.getUserId());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
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
}

