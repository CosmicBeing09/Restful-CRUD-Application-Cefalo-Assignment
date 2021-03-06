package com.crud.controller;

import com.crud.jwt.models.JwtRequest;
import com.crud.jwt.models.JwtResponse;
import com.crud.model.User;
import com.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping(value = "/user/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {"application/json","application/xml"})
    public ResponseEntity register(@Valid @RequestBody User user){
        return userService.createUser(user)? new ResponseEntity<>("Registration Successful", HttpStatus.CREATED)
                : new ResponseEntity("Username already taken",HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value = "/user/authenticate",produces = {"application/json","application/xml"},consumes = {"application/json","application/xml"})
    public ResponseEntity authenticate(@RequestBody JwtRequest request) throws Exception {
        return new ResponseEntity< >(new JwtResponse(userService.authenticate(request)),HttpStatus.OK);
    }


}

